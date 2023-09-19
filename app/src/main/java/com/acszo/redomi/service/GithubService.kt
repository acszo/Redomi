package com.acszo.redomi.service

import com.acszo.redomi.model.Release
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface GithubService {

    @GET("releases/latest")
    suspend fun getLatest(): Release

    @GET
    @Streaming
    suspend fun getLatestApk(@Url assetUrl: String): ResponseBody

}