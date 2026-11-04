package com.acszo.redomi.service

import com.acszo.redomi.model.DeezerMetadata
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DeezerService {
    @GET("track/{id}")
    suspend fun getTrack(@Path("id") id: String): Response<DeezerMetadata>

    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") id: String): Response<DeezerMetadata>
}
