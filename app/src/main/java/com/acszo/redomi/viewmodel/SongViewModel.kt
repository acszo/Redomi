package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.AppList
import com.acszo.redomi.model.Platform
import com.acszo.redomi.model.Providers
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.repository.DataStoreRepository
import com.acszo.redomi.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val songRepository: SongRepository
): ViewModel() {

    private val _songInfo: MutableStateFlow<SongInfo?> = MutableStateFlow(null)
    val songInfo: StateFlow<SongInfo?> = _songInfo.asStateFlow()

    private val _platforms: MutableStateFlow<Map<AppDetails, String>> = MutableStateFlow(emptyMap())
    val platforms: StateFlow<Map<AppDetails, String>> = _platforms.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getPlatforms(url: String, appList: AppList) = viewModelScope.launch {
        try {
            _isLoading.update { true }

            val response: Providers = songRepository.getSongs(url)
            _songInfo.update { response.entitiesByUniqueId.entries.first().value }

            val selectedApps = Platform.platforms.filter {
                if (appList == AppList.OPENING) {
                    val openingApps = dataStoreRepository.readDataStore().first().openingAppsSelection
                    openingApps.contains(it)
                } else {
                    val sharingApps = dataStoreRepository.readDataStore().first().sharingAppsSelection
                    sharingApps.contains(it)
                }
            }

            val mapLinkToApp: Map<AppDetails, String> = selectedApps
                .associateWith { response.linksByPlatform[it.title]?.url ?: "" }
                .filter { it.value.isNotEmpty() }

            _platforms.update { mapLinkToApp }

            _isLoading.update { false }
        } catch (e: Exception) {
            print(e.message)
        }
    }

}