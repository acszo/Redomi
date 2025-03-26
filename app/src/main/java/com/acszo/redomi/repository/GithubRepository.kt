package com.acszo.redomi.repository

import com.acszo.redomi.model.Release
import com.acszo.redomi.service.ApiResult
import com.acszo.redomi.service.GithubService
import com.acszo.redomi.service.apiCall
import okhttp3.ResponseBody

class GithubRepository(
    private val githubService: GithubService
) {

    suspend fun getLatest(): ApiResult<Release> = apiCall { githubService.getLatest() }

    suspend fun getLatestApk(assetUrl: String): ResponseBody = githubService.getLatestApk(assetUrl)

}