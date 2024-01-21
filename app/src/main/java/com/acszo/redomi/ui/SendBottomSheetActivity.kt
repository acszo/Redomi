package com.acszo.redomi.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.data.DataStoreConst
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.component.bottom_sheet.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.AppList
import com.acszo.redomi.viewmodel.UpdateViewModel
import com.acszo.redomi.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendBottomSheetActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val sendIntent = intent?.getStringExtra(Intent.EXTRA_TEXT)

            val songViewModel: SongViewModel = hiltViewModel()
            val updateViewModel: UpdateViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                songViewModel.getPlatforms(sendIntent.toString(), AppList.ALL)
                updateViewModel.latestRelease
            }

            val songInfo by songViewModel.songInfo.collectAsStateWithLifecycle()
            val platforms by songViewModel.platforms.collectAsStateWithLifecycle()
            val isLoading by songViewModel.isLoading.collectAsStateWithLifecycle()
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()

            val dataStore = SettingsDataStore(context)
            val currentTheme by dataStore.getThemeMode.collectAsStateWithLifecycle(initialValue = DataStoreConst.SYSTEM_THEME)

            RedomiTheme(
                currentTheme = currentTheme!!
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

