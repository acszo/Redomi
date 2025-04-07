package com.acszo.redomi.ui.page.bottom_sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.ui.common.enterFadeInTransition
import com.acszo.redomi.utils.IntentUtil.onIntentView
import com.acszo.redomi.viewmodel.BottomSheetUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    uiState: BottomSheetUiState,
    isActionSend: Boolean,
    isUpdateAvailable: Boolean,
    iconShape: Shape,
    listOrientation: ListOrientation,
    gridSize: Int
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = listOrientation == ListOrientation.HORIZONTAL
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    BottomSheetError(stringResource(uiState.error))
                }
                uiState.sourceSong != null -> {
                    val songs = uiState.songs
                    val selectedSong = remember { mutableStateOf(uiState.sourceSong) }
                    val showActionsMenu = remember { mutableStateOf(false) }

                    if (songs.size > 1 || isActionSend) {
                        SongCard(
                            song = selectedSong.value,
                            isUpdateAvailable = isUpdateAvailable
                        )
                    }

                    if (songs.size > 1 && !showActionsMenu.value) {
                        AppsList(
                            listOrientation = listOrientation,
                            songs = songs,
                            iconShape = iconShape,
                            gridSize = gridSize,
                            onClick = { song ->
                                if (!isActionSend) {
                                    onIntentView(context, song.link)
                                } else {
                                    showActionsMenu.value = true
                                    selectedSong.value = song
                                }
                            }
                        )
                    }

                    if (songs.size == 1) {
                        if (!isActionSend) {
                            Box(
                                modifier = Modifier.height(200.dp),
                            ) {
                                onIntentView(context, songs.first().link)
                                onDismiss()
                            }
                        } else {
                            showActionsMenu.value = true
                            selectedSong.value = songs.first()
                        }
                    }

                    AnimatedVisibility(
                        visible = showActionsMenu.value,
                        enter = enterFadeInTransition(),
                    ) {
                        ActionsMenu(url = selectedSong.value.link) {
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}