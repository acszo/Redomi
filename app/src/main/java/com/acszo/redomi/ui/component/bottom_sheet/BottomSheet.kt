package com.acszo.redomi.ui.component.bottom_sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.data.DataStoreConst.HORIZONTAL_LIST
import com.acszo.redomi.data.DataStoreConst.MEDIUM_GRID
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.ui.component.SongInfoDisplay
import com.acszo.redomi.utils.IntentUtil.onIntentView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    songInfo: SongInfo?,
    platforms: Map<AppDetails, String>,
    isLoading: Boolean,
    isActionsRequired: Boolean,
    isUpdateAvailable: Boolean
) {
    val context = LocalContext.current
    val dataStore = SettingsDataStore(context)
    val currentListType by dataStore.getLayoutListType.collectAsStateWithLifecycle(initialValue = HORIZONTAL_LIST)
    val currentGridSize by dataStore.getLayoutGridSize.collectAsStateWithLifecycle(initialValue = MEDIUM_GRID)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = currentListType == HORIZONTAL_LIST)
    val bringActions = remember { mutableStateOf(false) }
    val selectedPlatformLink = remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (platforms.size > 1 || platforms.size == 1 && isActionsRequired) {
                    SongInfoDisplay(
                        thumbnail = songInfo?.thumbnailUrl ?: "",
                        title = songInfo?.title ?: "",
                        artist = songInfo?.artistName ?: "",
                        isUpdateAvailable = isUpdateAvailable
                    )
                }

                if (platforms.size > 1) {
                    if (!bringActions.value) {
                        ItemsList(
                            listType = currentListType!!,
                            gridSize = currentGridSize!!,
                            platforms = platforms,
                            isActionsRequired = isActionsRequired,
                            bringActions = bringActions,
                            selectedPlatformLink = selectedPlatformLink
                        )
                    }
                } else {
                    if (!isActionsRequired) {
                        // if I don't, only the handle will be visible and it sucks >:(
                        Column(
                            modifier = Modifier.height(150.dp),
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        onIntentView(context, platforms.values.first())
                    } else {
                        bringActions.value = true
                        selectedPlatformLink.value = platforms.values.first()
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

@Composable
private fun ItemsList(
    listType: Int,
    gridSize: Int,
    platforms: Map<AppDetails, String>,
    isActionsRequired: Boolean,
    bringActions: MutableState<Boolean>,
    selectedPlatformLink: MutableState<String>
) {
    if (listType == HORIZONTAL_LIST) {
        LazyRow(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .height(150.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
        ) {
            items(items = platforms.toList()) { (app, link) ->
                AppItem(
                    appDetail = app,
                    link = link,
                    isActionsRequired = isActionsRequired,
                    bringActions = bringActions,
                    selectedPlatformLink = selectedPlatformLink,
                )
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier.padding(vertical = 15.dp),
            columns = GridCells.Fixed(gridSize),
            contentPadding = PaddingValues(horizontal = 10.dp),
        ) {
            items(items = platforms.toList()) { (app, link) ->
                AppItem(
                    appDetail = app,
                    link = link,
                    isActionsRequired = isActionsRequired,
                    bringActions = bringActions,
                    selectedPlatformLink = selectedPlatformLink,
                )
            }
        }
    }
}