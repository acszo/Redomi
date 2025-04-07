package com.acszo.redomi.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.model.Song
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
    val sourceSong: Song? = null,
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
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
        _bottomSheetUiState.update { it.copy(isLoading = true) }
        val response = songLinkRepository.getSongs(url)

        when(response) {
            is ApiResult.Success -> {
                val data = response.data
                val sourceSong = data.entitiesByUniqueId[data.entityUniqueId]
                val query = sourceSong?.run { Uri.encode("$title - $artistName") }

                val selectedApps = settingsDataStore.getSetOfStrings(key).first()
                val orderedApps = platforms.keys.filter { selectedApps.contains(it) }

                val songsInfo = orderedApps.mapNotNull { platform ->
                    val linksByPlatform = data.linksByPlatform[platform]

                    if (linksByPlatform != null) {
                        data.entitiesByUniqueId[linksByPlatform.entityUniqueId]
                            ?.copy(platform = platform, link = linksByPlatform.url)
                    } else {
                        Song(
                            isMatched = false,
                            platform = platform,
                            link = "${platforms[platform]?.searchUrl}$query",
                            type = null,
                            title = sourceSong?.title,
                            artistName = sourceSong?.artistName,
                            thumbnailUrl = null
                        )
                    }
                }

                _bottomSheetUiState.update {
                    it.copy(sourceSong = sourceSong, songs = songsInfo, isLoading = false)
                }
            }
            is ApiResult.Error -> {
                var message = when {
                    response.code in 400..499 -> R.string.error_incorrect_url
                    response.code in 500..599 -> R.string.error_server
                    else -> R.string.error_generic
                }

                _bottomSheetUiState.update {
                    it.copy(error = message, isLoading = false)
                }
            }
            is ApiResult.Exception -> {
                _bottomSheetUiState.update {
                    it.copy(error = response.message, isLoading = false)
                }
            }
        }
    }

}