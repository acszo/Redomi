package com.acszo.redomi.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acszo.redomi.data.DataStoreConst
import com.acszo.redomi.data.DataStoreConst.DARK_THEME
import com.acszo.redomi.data.DataStoreConst.LIGHT_THEME
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.component.bottom_sheet.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.AppList
import com.acszo.redomi.viewmodel.UpdateViewModel
import com.acszo.redomi.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendBottomSheetActivity: ComponentActivity() {
    private lateinit var songViewModel: SongViewModel
    private lateinit var updateViewModel: UpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val sendIntent = intent?.getStringExtra(Intent.EXTRA_TEXT)

            songViewModel = viewModel()
            LaunchedEffect(Unit) {
                songViewModel.getPlatforms(sendIntent.toString(), AppList.ALL)
            }
            val songInfo by songViewModel.songInfo.collectAsStateWithLifecycle()
            val platforms by songViewModel.platforms.collectAsStateWithLifecycle()
            val isLoading by songViewModel.isLoading.collectAsStateWithLifecycle()

            updateViewModel = viewModel()
            LaunchedEffect(Unit) {
                updateViewModel.latestRelease
            }
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()

            val dataStore = SettingsDataStore(context)
            val theme by dataStore.getThemeMode.collectAsStateWithLifecycle(initialValue = DataStoreConst.SYSTEM_THEME)
            val getTheme = when (theme) {
                DARK_THEME -> true
                LIGHT_THEME -> false
                else -> isSystemInDarkTheme()
            }

            RedomiTheme(
                darkTheme = getTheme
            ) {
                BottomSheet(
                    onDismiss = { this.finish() },
                    songInfo = songInfo,
                    platforms = platforms,
                    isLoading = isLoading,
                    isActionsRequired = true,
                    isUpdateAvailable = isUpdateAvailable
                )
            }
        }
    }
}

