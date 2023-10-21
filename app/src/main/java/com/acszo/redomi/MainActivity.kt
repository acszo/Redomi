package com.acszo.redomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acszo.redomi.data.DataStoreConst.DARK_THEME
import com.acszo.redomi.data.DataStoreConst.LIGHT_THEME
import com.acszo.redomi.data.DataStoreConst.SYSTEM_THEME
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.nav.RootNavigation
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.DataStoreViewModel
import com.acszo.redomi.viewmodel.UpdateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var updateViewModel: UpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()

        setContent {
            dataStoreViewModel = viewModel()
            updateViewModel = viewModel()
            val versionName = BuildConfig.VERSION_NAME
            LaunchedEffect(Unit) {
                updateViewModel.checkUpdate(versionName)
            }
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()

            val context = LocalContext.current
            val dataStore = SettingsDataStore(context)
            val theme by dataStore.getThemeMode.collectAsStateWithLifecycle(initialValue = SYSTEM_THEME)
            val getTheme = when (theme) {
                DARK_THEME -> true
                LIGHT_THEME -> false
                else -> isSystemInDarkTheme()
            }

            RedomiTheme(
                darkTheme = getTheme
            ) {
                RootNavigation(
                    dataStoreViewModel = dataStoreViewModel,
                    updateViewModel = updateViewModel,
                    isUpdateAvailable = isUpdateAvailable,
                )
            }
        }
    }
}