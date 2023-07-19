package com.acszo.redomi.repository

import com.acszo.redomi.model.Providers
import com.acszo.redomi.service.RetrofitInstance

class SongRepository {

    suspend fun getSongs(url: String): Providers = RetrofitInstance.api.getSongs(url)

}