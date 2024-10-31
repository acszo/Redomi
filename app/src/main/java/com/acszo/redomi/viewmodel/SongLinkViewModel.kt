package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.model.AppList
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.repository.DataStoreRepository
import com.acszo.redomi.repository.SongLinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongLinkViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val songLinkRepository: SongLinkRepository
): ViewModel() {

    private val _songInfo: MutableStateFlow<SongInfo?> = MutableStateFlow(null)
    val songInfo: StateFlow<SongInfo?> = _songInfo.asStateFlow()

    private val _platformsLink: MutableStateFlow<Map<String, String>> = MutableStateFlow(emptyMap())
    val platformsLink: StateFlow<Map<String, String>> = _platformsLink.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getPlatformsLink(url: String, appList: AppList) = viewModelScope.launch {
        try {
            _isLoading.update { true }

            val response = songLinkRepository.getSongs(url)
            _songInfo.update { response.entitiesByUniqueId.entries.first().value }

            val selectedApps = dataStoreRepository.readDataStore().first().let {
                if (appList == AppList.OPENING) it.openingAppsSelection else it.sharingAppsSelection
            }

            val orderedApps = platforms.keys.filter { selectedApps.contains(it) }

            val mapLinkToApp = orderedApps.associateWith {
                response.linksByPlatform[it]?.url ?: "${platforms[it]?.query}${_songInfo.value?.run { "$title - $artistName" }}"
            }

            _platformsLink.update { mapLinkToApp }

            _isLoading.update { false }
        } catch (e: Exception) {
            print(e.message)
        }
    }

}