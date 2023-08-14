package com.acszo.redomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.acszo.redomi.data.DataStoreConst.DARK_THEME
import com.acszo.redomi.data.DataStoreConst.LIGHT_THEME
import com.acszo.redomi.data.DataStoreConst.SYSTEM_THEME
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.nav.RootNavigation
import com.acszo.redomi.ui.theme.RedomiTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }

        setContent {
            val context = LocalContext.current
            val dataStore = SettingsDataStore(context)
            val theme = dataStore.getThemeMode.collectAsState(initial = SYSTEM_THEME)
            val getTheme = when (theme.value) {
                DARK_THEME -> true
                LIGHT_THEME -> false
                else -> isSystemInDarkTheme()
            }

            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !getTheme
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            RedomiTheme(
                darkTheme = getTheme
            ) {
                RootNavigation()
            }
        }
    }
}