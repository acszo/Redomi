package com.acszo.redomi.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.ui.bottom_sheet.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.AppList
import com.acszo.redomi.viewmodel.DataStoreViewModel
import com.acszo.redomi.viewmodel.SongViewModel
import com.acszo.redomi.viewmodel.UpdateViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
* ACTION_SEND -> shows sharing apps -> shows action menu
* ACTION_VIEW -> shows opening apps -> opens app
* */
@AndroidEntryPoint
class ConvertSongActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isActionSend = (intent.action == Intent.ACTION_SEND)

            val intentData = if (isActionSend) {
                intent?.getStringExtra(Intent.EXTRA_TEXT)
            } else {
                intent?.data
            }.toString()

            val appList = if (isActionSend) AppList.SHARING else AppList.OPENING

            val songViewModel: SongViewModel = hiltViewModel()
            val updateViewModel: UpdateViewModel = hiltViewModel()
            val dataStoreViewModel: DataStoreViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                songViewModel.getPlatforms(intentData, appList)
                updateViewModel.latestRelease
                dataStoreViewModel.getIconShape()
                dataStoreViewModel.getThemeMode()
            }

            val songInfo by songViewModel.songInfo.collectAsStateWithLifecycle()
            val platforms by songViewModel.platforms.collectAsStateWithLifecycle()
            val isLoading by songViewModel.isLoading.collectAsStateWithLifecycle()
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()
            val currentTheme by dataStoreViewModel.themeMode.collectAsStateWithLifecycle()

            RedomiTheme(
                currentTheme = currentTheme
            ) {
                BottomSheet(
                    onDismiss = { this.finish() },
                    dataStoreViewModel = dataStoreViewModel,
                    songInfo = songInfo,
                    platforms = platforms,
                    isLoading = isLoading,
                    isActionsRequired = isActionSend,
                    isUpdateAvailable = isUpdateAvailable
                )
            }
        }
    }
}

