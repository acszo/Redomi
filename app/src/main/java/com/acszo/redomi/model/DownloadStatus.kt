package com.acszo.redomi.model

sealed class DownloadStatus {

    data class Downloading(val progress: Int): DownloadStatus()
    data object Finished: DownloadStatus()

}
