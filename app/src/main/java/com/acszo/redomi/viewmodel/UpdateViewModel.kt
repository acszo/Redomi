package com.acszo.redomi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.R
import com.acszo.redomi.model.DownloadStatus
import com.acszo.redomi.model.Release
import com.acszo.redomi.repository.GithubRepository
import com.acszo.redomi.service.ApiResult
import com.acszo.redomi.utils.UpdateUtil.getApk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject

data class UpdatePageUiState(
    val latestRelease: Release? = null,
    val isLoading: Boolean = false,
    val error: Int? = null,
)

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {

    private val _updatePageUiState = MutableStateFlow(UpdatePageUiState())
    val updatePageUiState = _updatePageUiState.asStateFlow()

    private val _isUpdateAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUpdateAvailable = _isUpdateAvailable.asStateFlow()

    fun checkUpdate(currentVersion: Int) = viewModelScope.launch {
        _updatePageUiState.update { it.copy(isLoading = true) }

        when (val response = githubRepository.getLatest()) {
            is ApiResult.Success -> {
                val latestRelease = response.data
                _isUpdateAvailable.update {
                    currentVersion < convertToVersionCode(latestRelease.tagName)
                }
                _updatePageUiState.update {
                    it.copy(latestRelease = latestRelease, isLoading = false, error = null)
                }
            }
            is ApiResult.Error -> {
                val message = when  {
                    response.code in 400..499 -> R.string.update_info_failed
                    response.code in 500..599 -> R.string.error_server
                    else -> R.string.error_generic
                }

                _updatePageUiState.update {
                    it.copy(error = message, isLoading = false)
                }
            }
            is ApiResult.Exception -> {
                _updatePageUiState.update {
                    it.copy(error = response.message, isLoading = false)
                }
            }
        }
    }

    // it will always be 1 digit for Major and 2 digits for Minor and Patch
    fun convertToVersionCode(versionName: String): Int {
        val len = versionName.length - 1
        val versionMinor = if (versionName[3] == '.') "0${versionName[2]}" else versionName.substring(2, 4)
        val versionPatch = if (versionName[len - 1] == '.') "0${versionName[len]}" else versionName.substring(len - 1)

        return (versionName[0] + versionMinor + versionPatch).toInt()
    }

    suspend fun downloadApk(context: Context, release: Release): Flow<DownloadStatus> = withContext(Dispatchers.IO) {
        val assetUrl = release.assets[0].browserDownloadUrl
        try {
            return@withContext githubRepository.getLatestApk(assetUrl).saveApk(context)
        } catch (e: Exception) {
            print(e.message)
        }
        emptyFlow()
    }

    private fun ResponseBody.saveApk(context: Context): Flow<DownloadStatus> = flow {
        emit(DownloadStatus.Downloading(0))

        val apkFile = context.getApk()
        byteStream().use { inputStream ->
            apkFile.outputStream().use { outputStream ->
                val totalBytes = contentLength()
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var progressBytes = 0L
                var bytes = inputStream.read(buffer)

                while (bytes >= 0) {
                    outputStream.write(buffer, 0, bytes)
                    progressBytes += bytes
                    bytes = inputStream.read(buffer)
                    emit(DownloadStatus.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                }
            }
        }

        emit(DownloadStatus.Finished)
    }.flowOn(Dispatchers.IO).distinctUntilChanged()

}