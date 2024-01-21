package com.acszo.redomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.data.DataStoreConst.SYSTEM_THEME
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.nav.RootNavigation
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.UpdateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()

        setContent {
            val updateViewModel: UpdateViewModel = hiltViewModel()
            val versionName = BuildConfig.VERSION_NAME
            LaunchedEffect(Unit) {
                updateViewModel.checkUpdate(versionName)
            }
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()

            val context = LocalContext.current
            val dataStore = SettingsDataStore(context)
            val currentTheme by dataStore.getThemeMode.collectAsStateWithLifecycle(initialValue = SYSTEM_THEME)

            RedomiTheme(
                currentTheme = currentTheme!!
            ) {
                RootNavigation(
                    updateViewModel = updateViewModel,
                    isUpdateAvailable = isUpdateAvailable,
                )
            }
        }
    }
}