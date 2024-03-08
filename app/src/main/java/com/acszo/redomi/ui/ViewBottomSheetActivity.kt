package com.acszo.redomi.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.ui.component.bottom_sheet.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.utils.UpdateUtil.isAppInstalled
import com.acszo.redomi.viewmodel.AppList
import com.acszo.redomi.viewmodel.DataStoreViewModel
import com.acszo.redomi.viewmodel.SongViewModel
import com.acszo.redomi.viewmodel.UpdateViewModel
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
            val dataStoreViewModel: DataStoreViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                songViewModel.getPlatforms(viewIntent.toString(), AppList.INSTALLED)
                updateViewModel.latestRelease
                dataStoreViewModel.getThemeMode()
            }

            val songInfo by songViewModel.songInfo.collectAsStateWithLifecycle()
            val platforms by songViewModel.platforms.collectAsStateWithLifecycle()
            val isLoading by songViewModel.isLoading.collectAsStateWithLifecycle()
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()
            val currentTheme by dataStoreViewModel.themeMode.collectAsStateWithLifecycle()

            val installedApps: Map<AppDetails, String> = platforms.filter {
                var isInstalled = false
                for (packageName in it.key.packageName) {
                    if (isAppInstalled(context, packageName)) isInstalled = true
                }
                isInstalled
            }

            RedomiTheme(
                currentTheme = currentTheme
            ) {
                BottomSheet(
                    onDismiss = { this.finish() },
                    dataStoreViewModel = dataStoreViewModel,
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