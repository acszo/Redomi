package com.acszo.redomi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.model.DownloadStatus
import com.acszo.redomi.model.Release
import com.acszo.redomi.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {

    private val _latestRelease: MutableStateFlow<Release?> = MutableStateFlow(null)
    val latestRelease: StateFlow<Release?> = _latestRelease

    private val _isUpdateAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUpdateAvailable: StateFlow<Boolean> = _isUpdateAvailable

    fun getLatestRelease(currentVersion: String) = viewModelScope.launch {
        try {
            _latestRelease.update {
                githubRepository.getLatest()
            }
            _isUpdateAvailable.update {
                currentVersion != _latestRelease.value?.tag_name
            }
        }  catch (e: Exception) {
            print(e.message)
        }
    }

    suspend fun downloadApk(context: Context, release: Release): Flow<DownloadStatus> = withContext(Dispatchers.IO) {
        val assetName = release.assets[0].name
        val assetUrl = release.assets[0].browser_download_url
        try {
            return@withContext githubRepository.getLatestApk(assetUrl).saveApk(context, assetName)
        } catch (e: Exception) {
            print(e.message)
        }
        emptyFlow()
    }

    private fun ResponseBody.saveApk(context: Context, assetName: String): Flow<DownloadStatus> = flow {
        emit(DownloadStatus.Downloading(0))

        val apkFile = File(context.getExternalFilesDir("apk"), assetName)
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