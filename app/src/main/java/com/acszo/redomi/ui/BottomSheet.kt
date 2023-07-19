package com.acszo.redomi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import coil.compose.AsyncImage
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.SongInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    songInfo: SongInfo?,
    platforms: List<AppDetails>,
    isLoading: Boolean,
    onClickItem: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                SongInfoDisplay(songInfo?.thumbnailUrl, songInfo?.title, songInfo?.artistName)
                val sidePadding = (-24).dp
                LazyRow(
                    modifier = Modifier
                        .height(150.dp)
                        .layout { measurable, constraints ->
                            val placeable =
                                measurable.measure(constraints.offset(horizontal = -sidePadding.roundToPx() * 2))
                            layout(
                                placeable.width + sidePadding.roundToPx() * 2, placeable.height
                            ) {
                                placeable.place(+sidePadding.roundToPx(), 0)
                            }
                        },
                    contentPadding = PaddingValues(horizontal = 15.dp),
                ) {
                    items(items = platforms) { app ->
                        val title: String = app.title.replace("(?<=[^A-Z])(?=[A-Z])".toRegex(), " ")
                            .replaceFirstChar { it.uppercase() }
                        PlatformItem(onClickItem, title, app.image)
                    }
                }
            }
        }
    }
}

@Composable
private fun SongInfoDisplay(thumbnail: String?, title: String?, artist: String?) {
    Row(
        modifier = Modifier.padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        AsyncImage(
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(10.dp)),
            model = thumbnail ?: "",
            contentDescription = "Song Cover"
        )
        Column(
            modifier = Modifier.height(80.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = title ?: "",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = artist ?: "",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun PlatformItem(onClickItem: () -> Unit, title: String, image: Int) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClickItem() }
            .padding(5.dp, 15.dp, 5.dp, 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(id = image),
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentDescription = title
        )
        Text(text = title)
    }
}

