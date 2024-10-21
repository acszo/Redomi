package com.acszo.redomi.repository

import com.acszo.redomi.model.Providers
import com.acszo.redomi.service.SongLinkService

class SongLinkRepository(
    private val songLinkService: SongLinkService
) {

    suspend fun getSongs(url: String): Providers = songLinkService.getSongs(url)

}