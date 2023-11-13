package com.acszo.redomi.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.data.DataStoreConst
import com.acszo.redomi.data.DataStoreConst.DARK_THEME
import com.acszo.redomi.data.DataStoreConst.LIGHT_THEME
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.ui.component.bottom_sheet.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.utils.UpdateUtil.isAppInstalled
import com.acszo.redomi.viewmodel.AppList
import com.acszo.redomi.viewmodel.UpdateViewModel
import com.acszo.redomi.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewBottomSheetActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val viewIntent = intent?.data

            val songViewModel: SongViewModel = hiltViewModel()
            val updateViewModel: UpdateViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                songViewModel.getPlatforms(viewIntent.toString(), AppList.INSTALLED)
                updateViewModel.latestRelease
            }

            val songInfo by songViewModel.songInfo.collectAsStateWithLifecycle()
            val platforms by songViewModel.platforms.collectAsStateWithLifecycle()
            val isLoading by songViewModel.isLoading.collectAsStateWithLifecycle()
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()

            val installedApps: Map<AppDetails, String> = platforms.filter {
                var isInstalled = false
                for (packageName in it.key.packageName) {
                    if (isAppInstalled(context, packageName)) isInstalled = true
                }
                isInstalled
            }

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
                    platforms = installedApps,
                    isLoading = isLoading,
                    isActionsRequired = false,
                    isUpdateAvailable = isUpdateAvailable
                )
            }
        }
    }
}