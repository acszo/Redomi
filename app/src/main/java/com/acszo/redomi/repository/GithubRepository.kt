package com.acszo.redomi.repository

import com.acszo.redomi.model.Release
import com.acszo.redomi.service.GithubService

class GithubRepository(
    private val githubService: GithubService
) {

    suspend fun getLatest(): Release = githubService.getLatest()

}