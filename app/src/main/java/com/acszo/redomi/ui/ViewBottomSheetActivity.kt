package com.acszo.redomi.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acszo.redomi.data.DataStoreConst
import com.acszo.redomi.data.DataStoreConst.DARK_THEME
import com.acszo.redomi.data.DataStoreConst.LIGHT_THEME
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.ui.component.bottom_sheet.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.AppList
import com.acszo.redomi.viewmodel.UpdateViewModel
import com.acszo.redomi.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewBottomSheetActivity: ComponentActivity() {
    private lateinit var songViewModel: SongViewModel
    private lateinit var updateViewModel: UpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val viewIntent: Uri? = intent?.data

            songViewModel = viewModel()
            LaunchedEffect(Unit) {
                songViewModel.getPlatforms(viewIntent.toString(), AppList.INSTALLED)
            }
            val songInfo: SongInfo? = songViewModel.songInfo.collectAsState().value
            val platforms: Map<AppDetails, String> = songViewModel.platforms.collectAsState().value
            val isLoading: Boolean = songViewModel.isLoading.collectAsState().value

            updateViewModel = viewModel()
            LaunchedEffect(Unit) {
                updateViewModel.latestRelease
            }
            val isUpdateAvailable = updateViewModel.isUpdateAvailable.collectAsState().value

            val installedApps: Map<AppDetails, String> = platforms.filter {
                var isInstalled = false
                for (packageName in it.key.packageName) {
                    if (isAppInstalled(packageName)) isInstalled = true
                }
                isInstalled
            }

            val dataStore = SettingsDataStore(context)
            val theme = dataStore.getThemeMode.collectAsState(initial = DataStoreConst.SYSTEM_THEME)
            val getTheme = when (theme.value) {
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

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

}