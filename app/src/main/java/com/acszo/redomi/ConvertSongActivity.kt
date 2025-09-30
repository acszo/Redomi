package com.acszo.redomi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.data.AppList
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.ui.page.bottom_sheet.BottomSheet
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.viewmodel.DataStoreViewModel
import com.acszo.redomi.viewmodel.SongLinkViewModel
import com.acszo.redomi.viewmodel.UpdateViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
* ACTION_SEND -> shows sharing apps -> shows action menu
* ACTION_VIEW -> shows opening apps -> opens app
* */
@AndroidEntryPoint
class ConvertSongActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isActionSend = (intent.action == Intent.ACTION_SEND)
        var intentData: String
        var appList = AppList.OPENING

        setContent {
            if (isActionSend) {
                intentData = intent?.getStringExtra(Intent.EXTRA_TEXT)
                    ?.replaceFirst(".*(?=https://)".toRegex(), "").toString()
                appList = AppList.SHARING
            } else {
                intentData = intent?.data.toString()
            }

            val songLinkViewModel: SongLinkViewModel = hiltViewModel()
            val updateViewModel: UpdateViewModel = hiltViewModel()
            val dataStoreViewModel: DataStoreViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                songLinkViewModel.getPlatformsLink(intentData, appList.key)
                if (isGithubBuild) updateViewModel.checkUpdate(versionName)
            }

            val bottomSheetUiState by songLinkViewModel.bottomSheetUiState.collectAsStateWithLifecycle()
            val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()
            val theme by dataStoreViewModel.themeMode.collectAsStateWithLifecycle()
            val iconShape by dataStoreViewModel.iconShape.collectAsStateWithLifecycle()
            val listOrientation by dataStoreViewModel.listOrientation.collectAsStateWithLifecycle()
            val gridSize by dataStoreViewModel.gridSize.collectAsStateWithLifecycle()

            RedomiTheme(
                theme = theme
            ) {
                BottomSheet(
                    onDismiss = { this.finish() },
                    uiState = bottomSheetUiState,
                    isActionSend = isActionSend,
                    isUpdateAvailable = isUpdateAvailable,
                    iconShape = IconShape.entries[iconShape].shape,
                    listOrientation = ListOrientation.entries[listOrientation],
                    gridSize = gridSize,
                    refresh = { songLinkViewModel.getPlatformsLink(intentData, appList.key) }
                )
            }
        }
    }
}

