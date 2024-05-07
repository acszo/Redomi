package com.acszo.redomi.ui.bottom_sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.utils.IntentUtil.onIntentView
import com.acszo.redomi.viewmodel.DataStoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    dataStoreViewModel: DataStoreViewModel,
    songInfo: SongInfo?,
    platforms: Map<AppDetails, String>,
    isLoading: Boolean,
    isActionsRequired: Boolean,
    isUpdateAvailable: Boolean
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        dataStoreViewModel.getListOrientation()
        dataStoreViewModel.getGridSize()
    }

    val listOrientation by dataStoreViewModel.listOrientation.collectAsStateWithLifecycle()
    val gridSize by dataStoreViewModel.gridSize.collectAsStateWithLifecycle()
    val iconShape by dataStoreViewModel.iconShape.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = listOrientation == ListOrientation.HORIZONTAL.ordinal
    )
    val bringActions = remember { mutableStateOf(false) }
    val selectedPlatformLink = remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = WindowInsets.systemBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.height(200.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (platforms.isNotEmpty()) {
                    if (platforms.size > 1 || isActionsRequired) {
                        SongInfoDisplay(
                            type = songInfo?.type ?: "",
                            thumbnail = songInfo?.thumbnailUrl ?: "",
                            title = songInfo?.title ?: "",
                            artist = songInfo?.artistName ?: "",
                            isUpdateAvailable = isUpdateAvailable
                        )
                    }

                    if (platforms.size > 1 && !bringActions.value) {
                        when (ListOrientation.entries[listOrientation]) {
                            ListOrientation.HORIZONTAL -> {
                                HorizontalList(
                                    iconShape = iconShape,
                                    platforms = platforms,
                                    isActionsRequired = isActionsRequired,
                                    bringActions = bringActions,
                                    selectedPlatformLink = selectedPlatformLink
                                )
                            }
                            ListOrientation.VERTICAL -> {
                                VerticalList(
                                    gridSize = gridSize,
                                    iconShape = iconShape,
                                    platforms = platforms,
                                    isActionsRequired = isActionsRequired,
                                    bringActions = bringActions,
                                    selectedPlatformLink = selectedPlatformLink
                                )
                            }
                        }
                    }

                    if (platforms.size == 1) {
                        if (!isActionsRequired) {
                            Box(
                                modifier = Modifier.height(200.dp),
                            ) {
                                onIntentView(context, platforms.values.first())
                                onDismiss()
                            }
                        } else {
                            bringActions.value = true
                            selectedPlatformLink.value = platforms.values.first()
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.height(200.dp),
                    ) {
                        ResultNotFound()
                    }
                }

                AnimatedVisibility(
                    visible = bringActions.value,
                    enter = fadeIn(
                        tween(
                            durationMillis = 200,
                            easing = LinearEasing
                        )
                    ),
                ) {
                    ActionsMenu(url = selectedPlatformLink.value) {
                        onDismiss()
                    }
                }
            }
        }
    }
}