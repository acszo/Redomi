package com.acszo.redomi

import com.acszo.redomi.model.QobuzAlbum
import com.acszo.redomi.model.QobuzArtist
import com.acszo.redomi.model.QobuzEntity
import com.acszo.redomi.model.QobuzLink
import com.acszo.redomi.model.QobuzTrack
import com.acszo.redomi.model.QobuzTrackSearch
import com.acszo.redomi.model.Song
import com.acszo.redomi.model.Platform
import com.acszo.redomi.utils.QobuzBootstrap
import com.acszo.redomi.utils.QobuzMatcher
import com.acszo.redomi.utils.SharedLink
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Assert.*
import org.junit.Test

class QobuzParsingTest {
    private val source = Song(type = "song", title = "Example Song", artistName = "Example Artist")
    private val track = QobuzEntity(QobuzLink(QobuzLink.Type.TRACK, "42"),
        "Example Song", "Example Artist", null, isrc = "GBABC2400001")

    @Test fun parsesTrackHostsAndCanonicalizes() {
        for (host in listOf("open.qobuz.com", "play.qobuz.com", "OPEN.QOBUZ.COM")) {
            val link = QobuzLink.parse("https://$host/track/0042/?utm_source=share#player")
            assertEquals(QobuzLink(QobuzLink.Type.TRACK, "42"), link)
            assertEquals("https://open.qobuz.com/track/42", link?.url)
        }
    }

    @Test fun preservesAlphanumericAndZeroPrefixedAlbumIds() {
        for (id in listOf("s72ps0jshvpwa", "0066991040005")) {
            assertEquals(id, QobuzLink.parse("https://play.qobuz.com/album/$id")?.id)
            assertEquals(id, QobuzLink.parse("https://www.qobuz.com/fr-fr/album/a-title/$id")?.id)
            assertEquals(id, QobuzLink.parse("https://qobuz.com/us-en/album/a-title/$id")?.id)
        }
    }

    @Test fun rejectsLookalikeHostsCredentialsAndPorts() {
        for (url in listOf(
            "https://open.qobuz.com.evil.example/track/42",
            "https://open.qobuz.com@evil.example/track/42",
            "https://user@open.qobuz.com/track/42",
            "https://open.qobuz.com:8443/track/42",
            "ftp://open.qobuz.com/track/42"
        )) assertNull(url, QobuzLink.parse(url))
    }

    @Test fun rejectsUnsupportedAndMalformedPaths() {
        for (path in listOf("/playlist/42", "/artist/42", "/track/zero", "/track/0",
            "/track/-42", "/track/999999999999999999999", "/album/", "/album/%2fsecret",
            "/track/42/more", "/us-en/track/name/42", "/track/42/../bad")) {
            assertNull(path, QobuzLink.parse("https://open.qobuz.com$path"))
        }
    }

    @Test fun extractsSharedUrlWithoutTrailingTextOrPunctuation() {
        assertEquals("https://open.qobuz.com/track/42",
            SharedLink.extract("Listen to this: (https://open.qobuz.com/track/42).\nSent from Qobuz"))
        assertEquals("https://open.qobuz.com/album/abc",
            SharedLink.extract("https://open.qobuz.com/album/abc extra words"))
        assertEquals("https://example.com/song_(live)",
            SharedLink.extract("https://example.com/song_(live)"))
    }

    @Test fun extractsFirstUrlAndRejectsMissingUrls() {
        assertEquals("https://open.qobuz.com/track/42",
            SharedLink.extract("https://open.qobuz.com/track/42 https://example.com/"))
        assertNull(SharedLink.extract(null))
        assertNull(SharedLink.extract("no music link"))
    }

    @Test fun discoversOnlyTheTrustedBundle() {
        val html = """<script src="https://evil.example/main.js"></script>
            <script src="/resources/2.2.3/js/main.js"></script>"""
        assertEquals("https://open.qobuz.com/resources/2.2.3/js/main.js", QobuzBootstrap.bundleUrl(html))
        assertNull(QobuzBootstrap.bundleUrl("""<script src="https://evil.example/resources/1/js/main.js">"""))
        assertNull(QobuzBootstrap.bundleUrl("""<script src="//user@open.qobuz.com/resources/1/js/main.js">"""))
        assertNull(QobuzBootstrap.bundleUrl("""<script src="http://open.qobuz.com/resources/1/js/main.js">"""))
    }

    @Test fun bootstrapRejectsMissingOrAmbiguousIdentifiers() {
        assertEquals("123456789", QobuzBootstrap.appId("""config={app_id:"123456789"}"""))
        assertEquals("123", QobuzBootstrap.appId("""{"app_id": "123", again:{app_id:'123'}}"""))
        assertNull(QobuzBootstrap.appId("""{app_id:"123", other:{app_id:"456"}}"""))
        assertNull(QobuzBootstrap.appId("""{user_auth_token:"not-an-app-id"}"""))
    }

    @Test fun mapsMetadataIntoRedomiModels() {
        val json = Json { ignoreUnknownKeys = true }
        val entity = json.decodeFromString<QobuzTrack>("""{
            "id":42,"title":"Example Song","version":"Live",
            "performer":{"name":"Example Artist"},"isrc":"GBABC2400001",
            "album":{"id":"abc123","title":"Example Album","image":{"large":"https://example.com/cover.jpg"}},
            "extra_server_field":true
        }""").toEntity()!!
        val providers = entity.toProviders()
        val link = providers.linksByPlatform.getValue("qobuz")
        val song = providers.entitiesByUniqueId.getValue(link.entityUniqueId)
        assertEquals(link.entityUniqueId, providers.entityUniqueId)
        assertEquals("song", song.type)
        assertEquals("Example Song (Live)", song.title)
        assertEquals("Example Artist", song.artistName)
        assertEquals("https://example.com/cover.jpg", song.thumbnailUrl)
        assertEquals("https://open.qobuz.com/track/42", link.url)
        assertTrue(song.isMatched)
    }

    @Test fun usesAlbumArtistWhenTrackPerformerIsMissing() {
        val entity = QobuzTrack(42, "Example Song",
            album = QobuzAlbum("abc", artist = QobuzArtist("Example Artist"))).toEntity()
        assertEquals("Example Artist", entity?.artistName)
    }

    @Test fun rejectsIncompleteSourceMetadata() {
        assertNull(QobuzTrack(42, "Example Song").toEntity())
        assertNull(QobuzTrack(42, "", performer = QobuzArtist("Artist")).toEntity())
        assertNull(QobuzTrack(-1, "Example Song", performer = QobuzArtist("Artist")).toEntity())
        assertNull(QobuzAlbum("abc", "Album").toEntity())
    }

    @Test fun albumMappingPreservesTypeVersionAndId() {
        val entity = QobuzAlbum("0012345678905", "Example Album", "Deluxe",
            QobuzArtist("Artist"), upc = "0012345678905").toEntity()!!
        assertEquals("album", entity.toSong().type)
        assertEquals("Example Album (Deluxe)", entity.title)
        assertEquals("0012345678905", entity.link.id)
    }

    @Test fun doesNotRepeatVersionsAlreadyInTheTitle() {
        assertEquals("Example Song (Live)",
            QobuzTrack(42, "Example Song (Live)", "Live", QobuzArtist("Artist")).toEntity()?.title)
    }

    @Test(expected = SerializationException::class)
    fun missingSearchContainerIsNotMistakenForNoResults() {
        Json.decodeFromString<QobuzTrackSearch>("""{"status":"error"}""")
    }

    @Test fun matchesIdentifiersAcrossDisplayNameDifferences() {
        val candidate = track.copy(title = "Different display title", artistName = "Artist feat. Guest")
        assertEquals(candidate, QobuzMatcher.match(source, listOf(candidate), "gbabc2400001"))
    }

    @Test fun neverFallsBackToTheFirstWrongIdentifier() {
        assertNull(QobuzMatcher.match(source, listOf(track), "GBABC2400002"))
    }

    @Test fun rejectsWrongTypesAndUnavailableResults() {
        assertNull(QobuzMatcher.match(source, listOf(track.copy(streamable = false)), track.isrc))
        assertNull(QobuzMatcher.match(source,
            listOf(track.copy(link = QobuzLink(QobuzLink.Type.ALBUM, "abc"))), track.isrc))
    }

    @Test fun textMatchingIsConservativeAboutVersionsAndAmbiguity() {
        assertEquals(track, QobuzMatcher.match(source, listOf(track), null))
        assertNull(QobuzMatcher.match(source, listOf(track.copy(title = "Example Song (Live)")), null))
        assertNull(QobuzMatcher.match(source, listOf(track.copy(artistName = "Cover Artist")), null))
        assertNull(QobuzMatcher.match(source, listOf(track,
            track.copy(link = QobuzLink(QobuzLink.Type.TRACK, "43"))), null))
    }

    @Test fun normalizesTypographyButKeepsVersionWords() {
        assertEquals(track, QobuzMatcher.match(source.copy(title = "Example—Song"),
            listOf(track), null))
        assertNull(QobuzMatcher.match(source.copy(title = "Example Song Remastered"),
            listOf(track), null))
    }

    @Test fun handlesBarcodePaddingWithoutMatchingMissingCodes() {
        assertTrue(QobuzMatcher.sameUpc("0886443927087", "886443927087"))
        assertFalse(QobuzMatcher.sameUpc(null, null))
        assertFalse(QobuzMatcher.sameUpc("000", "0"))
        assertFalse(QobuzMatcher.sameUpc("ABC", "ABC"))
        assertFalse(QobuzMatcher.sameIsrc(null, null))
    }

    @Test fun qobuzIsRegisteredWithAnEncodedPublicCatalogSearch() {
        val platform = Platform.platforms.getValue("qobuz")
        val url = (platform.searchUrl + "Example%20Song%20Artist").toHttpUrl()
        assertEquals("Qobuz", platform.title)
        assertEquals("www.qobuz.com", url.host)
        assertEquals("/us-en/search", url.encodedPath)
        assertEquals("Example Song Artist", url.queryParameter("q"))
    }
}
