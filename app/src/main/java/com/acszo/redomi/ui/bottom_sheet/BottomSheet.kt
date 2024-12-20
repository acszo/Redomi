package com.acszo.redomi.ui.bottom_sheet

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
    val showActionsMenu = remember { mutableStateOf(false) }
    val selectedPlatformLink = remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when {
                !uiState.isLoaded -> {
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
                uiState.songInfo != null && uiState.platformsLinks != null -> {
                    val songInfo = uiState.songInfo
                    val platformsLinks = uiState.platformsLinks

                    if (platformsLinks.size > 1 || isActionSend) {
                        SongInfoDisplay(
                            type = songInfo.type,
                            thumbnail = songInfo.thumbnailUrl,
                            title = songInfo.title,
                            artist = songInfo.artistName,
                            isUpdateAvailable = isUpdateAvailable
                        )
                    }

                    if (platformsLinks.size > 1 && !showActionsMenu.value) {
                        when (listOrientation) {
                            ListOrientation.HORIZONTAL -> {
                                HorizontalList(
                                    platformsLink = platformsLinks,
                                    isActionSend = isActionSend,
                                    showActionsMenu = showActionsMenu,
                                    selectedPlatformLink = selectedPlatformLink,
                                    iconShape = iconShape
                                )
                            }

                            ListOrientation.VERTICAL -> {
                                VerticalList(
                                    platformsLink = platformsLinks,
                                    isActionSend = isActionSend,
                                    showActionsMenu = showActionsMenu,
                                    selectedPlatformLink = selectedPlatformLink,
                                    iconShape = iconShape,
                                    gridSize = gridSize
                                )
                            }
                        }
                    }

                    if (platformsLinks.size == 1) {
                        if (!isActionSend) {
                            Box(
                                modifier = Modifier.height(200.dp),
                            ) {
                                onIntentView(context, platformsLinks.values.first())
                                onDismiss()
                            }
                        } else {
                            showActionsMenu.value = true
                            selectedPlatformLink.value = platformsLinks.values.first()
                        }
                    }

                    AnimatedVisibility(
                        visible = showActionsMenu.value,
                        enter = enterFadeInTransition(),
                    ) {
                        ActionsMenu(url = selectedPlatformLink.value) {
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}