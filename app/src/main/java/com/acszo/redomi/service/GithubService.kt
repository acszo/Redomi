package com.acszo.redomi.service

import com.acszo.redomi.model.Release
import retrofit2.http.GET

interface GithubService {

    @GET("releases/latest")
    suspend fun getLatest(): Release

}