package com.acszo.redomi

import com.acszo.redomi.model.DeezerError
import com.acszo.redomi.model.DeezerMetadata
import com.acszo.redomi.model.Providers
import com.acszo.redomi.model.QobuzLink
import com.acszo.redomi.repository.QobuzRepository
import com.acszo.redomi.repository.SongLinkRepository
import com.acszo.redomi.service.ApiResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class QobuzRepositoryTest {
    private val qobuz = FakeQobuzService()
    private val deezer = FakeDeezerService()
    private val songLink = FakeSongLinkService()
    private val catalog = QobuzRepository(qobuz)
    private val repository = SongLinkRepository(songLink, catalog, deezer)
    private val trackLink = QobuzLink(QobuzLink.Type.TRACK, "42")

    private fun success(result: ApiResult<Providers>): Providers {
        assertTrue("Expected success, got $result", result is ApiResult.Success)
        return (result as ApiResult.Success).data
    }

    @Test fun inputTrackUsesIsrcBridgeAndKeepsTheQobuzSource() = runBlocking {
        val data = success(repository.getSongs(trackLink.url, "GB"))
        assertEquals(listOf("isrc:GBABC2400001"), deezer.requests)
        assertEquals(listOf("https://www.deezer.com/track/99"), songLink.urls)
        assertEquals(listOf("GB"), songLink.countries)
        assertEquals(trackLink.entityUniqueId, data.entityUniqueId)
        assertEquals("qobuz", data.entitiesByUniqueId.getValue(data.entityUniqueId).platform)
        assertEquals("https://song.link/d/99", data.pageUrl)
        assertTrue(data.linksByPlatform.containsKey("spotify"))
        assertFalse(data.lookupFailed)
    }

    @Test fun inputAlbumUsesBarcodeBridge() = runBlocking {
        songLink.providers = songLink.providers.copy(entitiesByUniqueId =
            songLink.providers.entitiesByUniqueId.mapValues { it.value.copy(type = "album") })
        val data = success(repository.getSongs("https://www.qobuz.com/fr-fr/album/example/abc123", "US"))
        assertEquals(listOf("upc:012345678905"), deezer.requests)
        assertEquals(listOf("https://www.deezer.com/album/99"), songLink.urls)
        assertEquals("album", data.entitiesByUniqueId.getValue(data.entityUniqueId).type)
    }

    @Test fun albumBridgeTriesEquivalentBarcodeWidthsWithoutDroppingRequiredZeros() = runBlocking {
        songLink.providers = songLink.providers.copy(entitiesByUniqueId =
            songLink.providers.entitiesByUniqueId.mapValues { it.value.copy(type = "album") })
        deezer.responsesById["upc:012345678905"] = DeezerMetadata(error = DeezerError(800))
        val data = success(repository.getSongs("https://open.qobuz.com/album/abc123", "US"))
        assertEquals(listOf("upc:012345678905", "upc:0012345678905"), deezer.requests)
        assertNotNull(data.pageUrl)
        assertFalse(data.lookupFailed)
    }

    @Test fun outgoingTrackAddsAReferentiallyValidExactMatch() = runBlocking {
        val data = success(repository.getSongs("https://open.spotify.com/track/example", "US"))
        assertEquals(songLink.providers.entityUniqueId, data.entityUniqueId)
        assertEquals(songLink.providers.linksByPlatform["spotify"], data.linksByPlatform["spotify"])
        val qobuzLink = data.linksByPlatform.getValue("qobuz")
        assertEquals(trackLink.url, qobuzLink.url)
        assertEquals("Example Song", data.entitiesByUniqueId.getValue(qobuzLink.entityUniqueId).title)
        assertEquals("GBABC2400001", qobuz.queries.first())
    }

    @Test fun deselectedQobuzAddsNoCatalogRequestsOrChanges() = runBlocking {
        val data = success(repository.getSongs("https://open.spotify.com/track/example", "US", false))
        assertEquals(songLink.providers, data)
        assertEquals(0, qobuz.pageCalls)
        assertTrue(deezer.requests.isEmpty())
    }

    @Test fun emptySearchIsAnUnmatchedDestinationNotTheFirstResult() = runBlocking {
        qobuz.tracks = emptyList()
        val data = success(repository.getSongs("https://open.spotify.com/track/example", "US"))
        assertFalse(data.linksByPlatform.containsKey("qobuz"))
        assertFalse(data.lookupFailed)
        assertEquals(2, qobuz.queries.size)
    }

    @Test fun wrongRecordingIsNotAddedEvenIfTitleAndArtistMatch() = runBlocking {
        qobuz.tracks = listOf(qobuz.track.copy(isrc = "GBABC2400002"))
        val data = success(repository.getSongs("https://open.spotify.com/track/example", "US"))
        assertFalse(data.linksByPlatform.containsKey("qobuz"))
    }

    @Test fun unavailableQobuzKeepsExistingProvidersAndReportsWarning() = runBlocking {
        qobuz.catalogError = 503
        val data = success(repository.getSongs("https://open.spotify.com/track/example", "US"))
        assertEquals(songLink.providers.linksByPlatform, data.linksByPlatform)
        assertTrue(data.lookupFailed)
    }

    @Test fun noBridgeRetainsValidSourceAndNoInventedUniversalLink() = runBlocking {
        deezer.metadata = DeezerMetadata(error = DeezerError(800))
        val data = success(repository.getSongs(trackLink.url, "US"))
        assertEquals(setOf("qobuz"), data.linksByPlatform.keys)
        assertNotNull(data.entitiesByUniqueId[data.entityUniqueId])
        assertNull(data.pageUrl)
        assertFalse(data.lookupFailed)
        assertTrue(songLink.urls.isEmpty())
    }

    @Test fun wrongBridgeIdentifierDoesNotConvertAnotherRecording() = runBlocking {
        deezer.metadata = deezer.metadata.copy(isrc = "GBABC2400002")
        val data = success(repository.getSongs(trackLink.url, "US"))
        assertEquals(setOf("qobuz"), data.linksByPlatform.keys)
        assertTrue(songLink.urls.isEmpty())
    }

    @Test fun bridgeNetworkFailureIsVisibleButPreservesSource() = runBlocking {
        deezer.error = 503
        val data = success(repository.getSongs(trackLink.url, "US"))
        assertTrue(data.lookupFailed)
        assertEquals(trackLink.entityUniqueId, data.entityUniqueId)
        assertNull(data.pageUrl)
    }

    @Test fun songLinkFailureIsVisibleButPreservesQobuzSource() = runBlocking {
        songLink.error = 429
        val data = success(repository.getSongs(trackLink.url, "US"))
        assertTrue(data.lookupFailed)
        assertEquals(trackLink.entityUniqueId, data.entityUniqueId)
    }

    @Test fun nonQobuzServiceErrorsKeepExistingErrorSemantics() = runBlocking {
        songLink.error = 400
        assertEquals(ApiResult.Error(400), repository.getSongs("https://open.spotify.com/track/example", "US"))
        assertEquals(0, qobuz.pageCalls)
    }

    @Test fun malformedProviderResponseIsNotABlankSuccess() = runBlocking {
        songLink.providers = songLink.providers.copy(entitiesByUniqueId = emptyMap())
        assertEquals(ApiResult.Error(502), repository.getSongs("https://open.spotify.com/track/example", "US"))
    }

    @Test fun unsupportedQobuzLinkFailsBeforeAnyNetworkCall() = runBlocking {
        assertEquals(ApiResult.Error(400), repository.getSongs("https://open.qobuz.com/playlist/42", "US"))
        assertEquals(0, qobuz.pageCalls)
        assertTrue(songLink.urls.isEmpty())
    }

    @Test fun malformedInputFailsBeforeAnyNetworkCall() = runBlocking {
        assertEquals(ApiResult.Error(400), repository.getSongs("", "US"))
        assertTrue(songLink.urls.isEmpty())
    }

    @Test fun deletedSourceRemainsAnError() = runBlocking {
        qobuz.catalogError = 404
        assertEquals(ApiResult.Error(404), repository.getSongs(trackLink.url, "US"))
        assertTrue(deezer.requests.isEmpty())
    }

    @Test fun mismatchedCatalogIdIsRejected() = runBlocking {
        qobuz.track = qobuz.track.copy(id = 43)
        assertEquals(ApiResult.Error(502), catalog.getEntity(trackLink))
    }

    @Test fun publicIdentifierIsCachedAcrossConcurrentRequests() = runBlocking {
        (1..4).map { async { catalog.getEntity(trackLink) } }.awaitAll()
        assertEquals(1, qobuz.pageCalls)
        assertEquals(1, qobuz.bundleCalls)
        assertEquals(4, qobuz.catalogCalls)
    }

    @Test fun rejectedPublicIdentifierIsRefreshedOnce() = runBlocking {
        qobuz.authenticationFailures = 1
        assertTrue(catalog.getEntity(trackLink) is ApiResult.Success)
        assertEquals(2, qobuz.pageCalls)
        assertEquals(2, qobuz.catalogCalls)
    }

    @Test fun permanentAuthorizationFailureDoesNotLoop() = runBlocking {
        qobuz.catalogError = 403
        assertEquals(ApiResult.Error(403), catalog.getEntity(trackLink))
        assertEquals(2, qobuz.catalogCalls)
    }

    @Test fun bootstrapChangesBecomeExplicitErrors() = runBlocking {
        qobuz.html = "<html>No bundle</html>"
        assertTrue(catalog.getEntity(trackLink) is ApiResult.Exception)
        assertEquals(0, qobuz.catalogCalls)
    }

    @Test fun networkExceptionsRemainErrors() = runBlocking {
        qobuz.exception = IOException("offline")
        assertEquals(ApiResult.Exception(R.string.error_no_internet_connection), catalog.getEntity(trackLink))
    }

    @Test(expected = CancellationException::class)
    fun coroutineCancellationIsNeverConvertedIntoAFallback() = runBlocking {
        qobuz.exception = CancellationException("cancelled")
        repository.getSongs(trackLink.url, "US")
        Unit
    }
}
