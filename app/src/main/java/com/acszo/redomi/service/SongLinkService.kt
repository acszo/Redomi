package com.acszo.redomi.service

import com.acszo.redomi.model.Providers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SongLinkService {

    @GET("links")
    suspend fun getSongs(@Query("url") url: String, @Query("userCountry") countryCode: String): Response<Providers>

}