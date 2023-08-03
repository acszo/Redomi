package com.acszo.redomi.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.acszo.redomi.MainActivity
import com.acszo.redomi.R
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.SongInfo
import com.acszo.redomi.ui.component.ClickableItem
import com.acszo.redomi.util.Clipboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    songInfo: SongInfo?,
    platforms: List<AppDetails>,
    isLoading: Boolean,
    isActionsRequired: Boolean
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bringActions = remember { mutableStateOf(false) }
    val selectedPlatformLink = remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStore = SettingsDataStore(context)
    val listType = dataStore.getLayoutListType.collectAsState(initial = "")
    val gridSize = dataStore.getLayoutGridSize.collectAsState(initial = 0)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(
                        bottom = WindowInsets.systemBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                SongInfoDisplay(
                    thumbnail = songInfo?.thumbnailUrl ?: "",
                    title = songInfo?.title ?: "",
                    artist = songInfo?.artistName ?: ""
                )
                if (!bringActions.value) {
                    LazyListType(
                        listType = listType.value!!,
                        gridSize = gridSize.value!!,
                        platforms= platforms,
                        isActionsRequired = isActionsRequired,
                        bringActions = bringActions,
                        selectedPlatformLink = selectedPlatformLink
                    )
                } else {
                    val clipboardManager: ClipboardManager = LocalClipboardManager.current
                    Row(
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                            .height(150.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
                    ) {
                        ActionsMenuItem(R.string.open, R.drawable.play_fill_icon) {
                            onIntentView(
                                context = context,
                                url = selectedPlatformLink.value
                            )
                        }
                        ActionsMenuItem(R.string.copy, R.drawable.link_fill_icon) {
                            Clipboard().copyText(
                                clipboardManager = clipboardManager,
                                text = selectedPlatformLink.value,
                                onDismiss = onDismiss
                            )
                        }
                        ActionsMenuItem(R.string.share, R.drawable.share_fill_icon) {
                            onIntentSend(
                                context = context,
                                url = selectedPlatformLink.value,
                                onDismiss = onDismiss
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyListType(
    listType: String,
    gridSize: Int,
    platforms: List<AppDetails>,
    isActionsRequired: Boolean,
    bringActions: MutableState<Boolean>,
    selectedPlatformLink: MutableState<String>
) {
    if (listType == stringResource(id = R.string.vertical_list)) {
        LazyVerticalGrid(
            modifier = Modifier.padding(vertical = 15.dp),
            columns = GridCells.Fixed(gridSize),
            contentPadding = PaddingValues(horizontal = 10.dp),
        ) {
            items(items = platforms) { app ->
                AppItem(
                    app,
                    isActionsRequired,
                    bringActions,
                    selectedPlatformLink
                )
            }
        }
    } else {
        LazyRow(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .height(150.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
        ) {
            items(items = platforms) { app ->
                AppItem(
                    app,
                    isActionsRequired,
                    bringActions,
                    selectedPlatformLink
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongInfoDisplay(thumbnail: String, title: String, artist: String) {
    val context = LocalContext.current
    val image = rememberAsyncImagePainter(model = thumbnail)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (image.state is AsyncImagePainter.State.Loading) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter = painterResource(id = R.drawable.song_fill_icon),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = stringResource(id = R.string.placeholder)
                )
            }
            Image(
                modifier = Modifier.size(80.dp),
                painter = image,
                contentScale = ContentScale.FillHeight,
                contentDescription = stringResource(id = R.string.song_cover),
            )
        }
        Column(
            modifier = Modifier
                .height(80.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = title,
                maxLines  = 2,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                )
            )
            Text(
                text = artist,
                maxLines  = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                ),
            )
        }

        IconButton(
            onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.settings_icon),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = stringResource(id = R.string.settings)
            )
        }
    }
}

@Composable
private fun AppItem(
    appDetail: AppDetails,
    isActionsRequired: Boolean,
    bringActions: MutableState<Boolean>,
    selectedPlatformLink: MutableState<String>
) {
    val context = LocalContext.current
    val title: String = appDetail.title.replace("(?<=[^A-Z])(?=[A-Z])".toRegex(), " ")
        .replaceFirstChar { it.uppercase() }
    val titleWords: List<String> = title.split("\\s+".toRegex())

    ClickableItem(
        @Composable {
            Image(
                painterResource(id = appDetail.icon),
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                contentDescription = titleWords[0],
            )
            Text(text = titleWords[0].trim())
            Text(text = if (titleWords.size > 1) titleWords[1] else "")
        },
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable {
                if (!isActionsRequired) {
                    onIntentView(context, appDetail.link)
                } else {
                    bringActions.value = true
                    selectedPlatformLink.value = appDetail.link
                }
            }
    )
}

@Composable
private fun ActionsMenuItem(label: Int, icon: Int, onClickAction: () -> Unit) {
    ClickableItem(
        @Composable {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClickAction()
                    }
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .size(70.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(id = icon),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = stringResource(label)
                )
            }
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}

private fun onIntentView(context: Context, url: String) {
    val uri: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

private fun onIntentSend(context: Context, url: String, onDismiss: () -> Unit) {
    val intent = Intent(Intent.ACTION_SEND)
        .putExtra(Intent.EXTRA_TEXT, url)
        .setType("text/plain")
    context.startActivity(Intent.createChooser(intent, null))
    onDismiss()
}