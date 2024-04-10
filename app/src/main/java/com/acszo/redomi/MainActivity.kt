package com.acszo.redomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.ui.nav.RootNavigation
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.utils.UpdateUtil.deleteApk
import com.acszo.redomi.viewmodel.DataStoreViewModel
import com.acszo.redomi.viewmodel.UpdateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()

        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
            val updateViewModel: UpdateViewModel = hiltViewModel()
            val versionName = BuildConfig.VERSION_NAME

            LaunchedEffect(Unit) {
                updateViewModel.checkUpdate(versionName)
                dataStoreViewModel.getIsFirstTime()
                dataStoreViewModel.getLayoutListType()
                dataStoreViewModel.getLayoutGridSize()
                dataStoreViewModel.getThemeMode()
                scope.launch(Dispatchers.IO) {
                    deleteApk(context)
                }
            }

            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()
            val currentTheme by dataStoreViewModel.themeMode.collectAsStateWithLifecycle()

            RedomiTheme(
                currentTheme = currentTheme
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