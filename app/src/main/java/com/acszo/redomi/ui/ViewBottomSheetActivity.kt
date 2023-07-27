package com.acszo.redomi.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.SongViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class ViewBottomSheetActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            val viewIntent: Uri? = intent?.data
            val songViewModel = viewModel<SongViewModel>()
            LaunchedEffect(Unit) { songViewModel.getProviders(viewIntent.toString()) }
            val songInfo: SongInfo? = songViewModel.songInfo.collectAsState().value
            val platforms: List<AppDetails> = songViewModel.platforms.collectAsState().value
            val isLoading: Boolean = songViewModel.isLoading.collectAsState().value

            val installedApps: List<AppDetails> = platforms.filter {
                var isInstalled = false
                for (packageName in it.packageName) {
                    if (isAppInstalled(packageName)) isInstalled = true
                }
                isInstalled
            }

            RedomiTheme {
                BottomSheet(
                    onDismiss = { this.finish() },
                    songInfo = songInfo,
                    platforms = installedApps,
                    isLoading = isLoading,
                    isActionsRequired = false
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