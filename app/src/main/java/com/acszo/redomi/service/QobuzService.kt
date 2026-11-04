package com.acszo.redomi.service

import com.acszo.redomi.model.QobuzAlbum
import com.acszo.redomi.model.QobuzAlbumSearch
import com.acszo.redomi.model.QobuzTrack
import com.acszo.redomi.model.QobuzTrackSearch
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface QobuzService {
    @GET("https://open.qobuz.com/")
    suspend fun getWebPage(): Response<ResponseBody>

    @GET
    suspend fun getWebBundle(@Url url: String): Response<ResponseBody>

    @GET("track/get")
    suspend fun getTrack(@Query("track_id") id: String, @Query("app_id") appId: String): Response<QobuzTrack>

    @GET("album/get")
    suspend fun getAlbum(@Query("album_id") id: String, @Query("app_id") appId: String): Response<QobuzAlbum>

    @GET("track/search")
    suspend fun searchTracks(
        @Query("query") query: String,
        @Query("app_id") appId: String,
        @Query("limit") limit: Int = 20
    ): Response<QobuzTrackSearch>

    @GET("album/search")
    suspend fun searchAlbums(
        @Query("query") query: String,
        @Query("app_id") appId: String,
        @Query("limit") limit: Int = 20
    ): Response<QobuzAlbumSearch>
}
