package com.acszo.redomi.ui

import android.content.Intent
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

class SendBottomSheetActivity: ComponentActivity() {

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

            val sendIntent: String? = intent?.getStringExtra(Intent.EXTRA_TEXT)
            val songViewModel = viewModel<SongViewModel>()
            LaunchedEffect(Unit) { songViewModel.getProviders(sendIntent.toString()) }
            val songCover: SongInfo? = songViewModel.songInfo.collectAsState().value
            val platforms: List<AppDetails> = songViewModel.platforms.collectAsState().value
            val isLoading: Boolean = songViewModel.isLoading.collectAsState().value

            RedomiTheme {
                BottomSheet(
                    onDismiss = { this.finish() },
                    songInfo = songCover,
                    platforms = platforms,
                    isLoading = isLoading,
                    onClickItem = { }
                )
            }
        }
    }

    private fun actionSheet() {

    }

}

