package com.acszo.redomi

import com.acszo.redomi.di.AppModule
import com.acszo.redomi.model.Providers
import com.acszo.redomi.repository.SongLinkRepository
import com.acszo.redomi.service.ApiResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test

class QobuzLiveTest {
    private lateinit var repository: SongLinkRepository

    @Before fun configure() {
        assumeTrue("Set REDOMI_LIVE_TESTS=true to run public catalog checks",
            System.getenv("REDOMI_LIVE_TESTS") == "true")
        val converter = AppModule.jsonConverter()
        val client = AppModule.provideCatalogClient()
        repository = AppModule.provideSongRepository(
            AppModule.provideSongLinkService(converter),
            AppModule.provideQobuzRepository(AppModule.provideQobuzService(converter, client)),
            AppModule.provideDeezerService(converter, client)
        )
    }

    private fun success(result: ApiResult<Providers>): Providers {
        assertTrue("Expected live success, got $result", result is ApiResult.Success)
        return (result as ApiResult.Success).data
    }

    @Test fun qobuzTrackResolvesToOtherServices() = runBlocking {
        val data = success(repository.getSongs("https://open.qobuz.com/track/9140029", "US"))
        assertEquals("QOBUZ_SONG::9140029", data.entityUniqueId)
        assertTrue(data.entitiesByUniqueId.getValue(data.entityUniqueId).title!!.contains("Lose Yourself to Dance"))
        assertTrue(data.linksByPlatform.containsKey("spotify"))
        assertTrue(data.linksByPlatform.containsKey("appleMusic"))
        assertNotNull(data.pageUrl)
        assertFalse(data.lookupFailed)
    }

    @Test fun qobuzAlbumResolvesToOtherServices() = runBlocking {
        val data = success(repository.getSongs("https://play.qobuz.com/album/0886443927087", "US"))
        assertEquals("album", data.entitiesByUniqueId.getValue(data.entityUniqueId).type)
        assertTrue(data.linksByPlatform.containsKey("spotify"))
        assertNotNull(data.pageUrl)
        assertFalse(data.lookupFailed)
    }

    @Test fun otherServiceTrackResolvesToQobuz() = runBlocking {
        val data = success(repository.getSongs("https://www.deezer.com/track/67238733", "US"))
        val link = data.linksByPlatform.getValue("qobuz")
        assertTrue(link.url.startsWith("https://open.qobuz.com/track/"))
        assertTrue(data.entitiesByUniqueId.getValue(link.entityUniqueId).title!!.contains("Lose Yourself to Dance"))
        assertEquals("song", data.entitiesByUniqueId.getValue(link.entityUniqueId).type)
        assertFalse(data.lookupFailed)
    }

    @Test fun otherServiceAlbumResolvesToQobuz() = runBlocking {
        val data = success(repository.getSongs("https://www.deezer.com/album/6575789", "US"))
        val link = data.linksByPlatform.getValue("qobuz")
        assertTrue(link.url.startsWith("https://open.qobuz.com/album/"))
        assertEquals("Random Access Memories", data.entitiesByUniqueId.getValue(link.entityUniqueId).title)
        assertEquals("album", data.entitiesByUniqueId.getValue(link.entityUniqueId).type)
        assertFalse(data.lookupFailed)
    }
}
