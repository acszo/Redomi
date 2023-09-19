package com.acszo.redomi.repository

import com.acszo.redomi.model.Release
import com.acszo.redomi.service.GithubService
import okhttp3.ResponseBody

class GithubRepository(
    private val githubService: GithubService
) {

    suspend fun getLatest(): Release = githubService.getLatest()

    suspend fun getLatestApk(assetUrl: String): ResponseBody = githubService.getLatestApk(assetUrl)

}