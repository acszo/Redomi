package com.acszo.redomi.service

import com.acszo.redomi.R
import okio.IOException
import retrofit2.Response
import java.net.SocketTimeoutException

object Api {

    const val BASE_URL_SONG_LINK = "https://api.song.link/v1-alpha.1/"
    const val BASE_URL_GITHUB = "https://api.github.com/repos/acszo/Redomi/"

    const val SEARCH_QUERY_YT_MUSIC = "https://music.youtube.com/search?q="
    const val SEARCH_QUERY_YOUTUBE = "https://www.youtube.com/results?search_query="
    const val SEARCH_QUERY_SPOTIFY = "https://open.spotify.com/search/results/"
    const val SEARCH_QUERY_DEEZER = "https://www.deezer.com/search/"
    const val SEARCH_QUERY_TIDAL = "https://listen.tidal.com/search?q="
    const val SEARCH_QUERY_AMAZON_MUSIC = "https://music.amazon.com/search/"
    const val SEARCH_QUERY_APPLE_MUSIC = "https://music.apple.com/search?term="
    const val SEARCH_QUERY_SOUNDCLOUD = "https://soundcloud.com/search?q="
    const val SEARCH_QUERY_NAPSTER = "https://napster.com/search?query="
    const val SEARCH_QUERY_YANDEX = "https://music.yandex.ru/search?text="
    const val SEARCH_QUERY_ANGHAMI = "https://play.anghami.com/search/"

}

sealed class ApiResult<T: Any> {
    data class Success<T: Any>(val data: T): ApiResult<T>()
    data class Error(val code: Int): ApiResult<Nothing>()
    data class Exception(val message: Int): ApiResult<Nothing>()
}

suspend fun <T: Any> apiCall(
    execute: suspend() -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            ApiResult.Error(response.code())
        }
    } catch(_: SocketTimeoutException) {
        ApiResult.Exception(R.string.error_timeout)
    } catch (_: IOException) {
        ApiResult.Exception(R.string.error_no_internet_connection)
    } catch (_: Exception) {
        ApiResult.Exception(R.string.error_generic)
    } as ApiResult<T>
}