package com.acszo.redomi.service

import com.acszo.redomi.model.Providers
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {

    @GET("links")
    suspend fun getSongs(@Query("url") url: String): Providers

}