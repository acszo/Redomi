package com.acszo.redomi.repository

import com.acszo.redomi.model.Providers
import com.acszo.redomi.service.ApiResult
import com.acszo.redomi.service.SongLinkService
import com.acszo.redomi.service.apiCall

class SongLinkRepository(
    private val songLinkService: SongLinkService
) {

    suspend fun getSongs(url: String): ApiResult<Providers> = apiCall { songLinkService.getSongs(url) }

}