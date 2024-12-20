package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.repository.SongLinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.acszo.redomi.R
import com.acszo.redomi.service.ApiResult
import javax.inject.Inject

data class BottomSheetUiState(
    val songInfo: SongInfo? = null,
    val platformsLinks: Map<String, String>? = null,
    val isLoaded: Boolean = false,
    val error: Int? = null
)

@HiltViewModel
class SongLinkViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val songLinkRepository: SongLinkRepository
): ViewModel() {

    private val _bottomSheetUiState = MutableStateFlow(BottomSheetUiState())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    fun getPlatformsLink(url: String, key: String) = viewModelScope.launch {
        val response = songLinkRepository.getSongs(url)

        when(response) {
            is ApiResult.Success -> {
                val data = response.data
                val songInfo = data.entitiesByUniqueId.entries.first().value
                val selectedApps = settingsDataStore.getSetOfStrings(key).first()
                val orderedApps = platforms.keys.filter { selectedApps.contains(it) }
                val mapLinkToApp = orderedApps.associateWith {
                    data.linksByPlatform[it]?.url ?: "${platforms[it]?.query}${songInfo.run { "$title - $artistName" }}"
                }

                _bottomSheetUiState.update {
                    it.copy(songInfo = songInfo, platformsLinks = mapLinkToApp, isLoaded = true)
                }
            }
            is ApiResult.Error -> {
                var message = when  {
                    response.code in 400..499 -> R.string.error_incorrect_url
                    response.code in 500..599 -> R.string.error_server
                    else -> R.string.error_generic
                }

                _bottomSheetUiState.update {
                    it.copy(error = message, isLoaded = true)
                }
            }
            is ApiResult.Exception -> {
                _bottomSheetUiState.update {
                    it.copy(error = response.message, isLoaded = true)
                }
            }
        }
    }

}