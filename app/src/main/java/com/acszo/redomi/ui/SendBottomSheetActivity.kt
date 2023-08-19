package com.acszo.redomi.ui

import android.content.Intent
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
import com.acszo.redomi.ui.component.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.SongViewModel

class SendBottomSheetActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sendIntent: String? = intent?.getStringExtra(Intent.EXTRA_TEXT)
            val songViewModel = viewModel<SongViewModel>()
            LaunchedEffect(Unit) { songViewModel.getProviders(sendIntent.toString()) }
            val songInfo: SongInfo? = songViewModel.songInfo.collectAsState().value
            val platforms: List<AppDetails> = songViewModel.platforms.collectAsState().value
            val isLoading: Boolean = songViewModel.isLoading.collectAsState().value

            val context = LocalContext.current
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
                    platforms = platforms,
                    isLoading = isLoading,
                    isActionsRequired = true
                )
            }
        }
    }

}

