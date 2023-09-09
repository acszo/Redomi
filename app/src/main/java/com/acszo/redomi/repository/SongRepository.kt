package com.acszo.redomi.repository

import com.acszo.redomi.model.Providers
import com.acszo.redomi.service.SongService

class SongRepository(
    private val songService: SongService
) {

    suspend fun getSongs(url: String): Providers = songService.getSongs(url)

}