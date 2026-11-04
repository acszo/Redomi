package com.acszo.redomi

import com.acszo.redomi.model.*
import com.acszo.redomi.service.DeezerService
import com.acszo.redomi.service.QobuzService
import com.acszo.redomi.service.SongLinkService
import kotlinx.coroutines.yield
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal class FakeQobuzService : QobuzService {
    val album = QobuzAlbum("abc123", "Example Album", artist = QobuzArtist("Example Artist"),
        upc = "0012345678905")
    var track = QobuzTrack(42, "Example Song", performer = QobuzArtist("Example Artist"),
        album = album, isrc = "GBABC2400001")
    var tracks = listOf(track)
    var albums = listOf(album)
    var html = """<script src="/resources/1/js/main.js"></script>"""
    var script = """config={app_id:"123456789"}"""
    var pageCalls = 0
    var bundleCalls = 0
    var catalogCalls = 0
    var catalogError: Int? = null
    var authenticationFailures = 0
    var exception: Exception? = null
    val queries = mutableListOf<String>()

    override suspend fun getWebPage(): Response<ResponseBody> {
        pageCalls++
        yield()
        return Response.success(html.toResponseBody())
    }

    override suspend fun getWebBundle(url: String): Response<ResponseBody> {
        bundleCalls++
        return Response.success(script.toResponseBody())
    }

    private fun <T> response(data: T): Response<T> {
        catalogCalls++
        exception?.let { throw it }
        if (authenticationFailures > 0) {
            authenticationFailures--
            return Response.error(401, "{}".toResponseBody())
        }
        return catalogError?.let { Response.error(it, "{}".toResponseBody()) } ?: Response.success(data)
    }

    override suspend fun getTrack(id: String, appId: String): Response<QobuzTrack> = response(track)
    override suspend fun getAlbum(id: String, appId: String): Response<QobuzAlbum> = response(album)
    override suspend fun searchTracks(query: String, appId: String, limit: Int): Response<QobuzTrackSearch> {
        queries += query
        return response(QobuzTrackSearch(QobuzTracks(tracks)))
    }
    override suspend fun searchAlbums(query: String, appId: String, limit: Int): Response<QobuzAlbumSearch> {
        queries += query
        return response(QobuzAlbumSearch(QobuzAlbums(albums)))
    }
}

internal class FakeDeezerService : DeezerService {
    var metadata = DeezerMetadata(99, "GBABC2400001", "0012345678905")
    var error: Int? = null
    val requests = mutableListOf<String>()
    val responsesById = mutableMapOf<String, DeezerMetadata>()

    private fun response(id: String): Response<DeezerMetadata> {
        requests += id
        return error?.let { Response.error(it, "{}".toResponseBody()) }
            ?: Response.success(responsesById[id] ?: metadata)
    }

    override suspend fun getTrack(id: String): Response<DeezerMetadata> = response(id)
    override suspend fun getAlbum(id: String): Response<DeezerMetadata> = response(id)
}

internal class FakeSongLinkService : SongLinkService {
    var providers = Providers(
        "DEEZER_SONG::99",
        mapOf("deezer" to Link("https://www.deezer.com/track/99", "DEEZER_SONG::99"),
            "spotify" to Link("https://open.spotify.com/track/example", "SPOTIFY_SONG::example")),
        mapOf("DEEZER_SONG::99" to Song(type = "song", title = "Example Song", artistName = "Example Artist"),
            "SPOTIFY_SONG::example" to Song(type = "song", title = "Example Song", artistName = "Example Artist")),
        pageUrl = "https://song.link/d/99"
    )
    var error: Int? = null
    val urls = mutableListOf<String>()
    val countries = mutableListOf<String>()

    override suspend fun getSongs(url: String, key: String, countryCode: String): Response<Providers> {
        urls += url
        countries += countryCode
        return error?.let { Response.error(it, "{}".toResponseBody()) } ?: Response.success(providers)
    }
}
