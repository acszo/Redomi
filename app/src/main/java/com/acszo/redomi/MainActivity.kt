package com.acszo.redomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
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

        setContent {
            dataStoreViewModel = viewModel()
            updateViewModel = viewModel()
            val versionName = BuildConfig.VERSION_NAME
            LaunchedEffect(Unit) {
                updateViewModel.checkUpdate(versionName)
            }
            val isUpdateAvailable = updateViewModel.isUpdateAvailable.collectAsState().value

            val context = LocalContext.current
            val dataStore = SettingsDataStore(context)
            val theme = dataStore.getThemeMode.collectAsState(initial = SYSTEM_THEME)
            val getTheme = when (theme.value) {
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