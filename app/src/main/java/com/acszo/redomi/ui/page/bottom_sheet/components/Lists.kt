package com.acszo.redomi.ui.page.bottom_sheet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.model.Song
import com.acszo.redomi.ui.common.ClickableItem

@Composable
fun AppsList(
    listOrientation: ListOrientation,
    songs: List<Song>,
    iconShape: Shape,
    gridSize: Int,
    onClick: (song: Song) -> Unit
) {
    when (listOrientation) {
        ListOrientation.HORIZONTAL -> {
            HorizontalList(
                songs = songs,
                iconShape = iconShape,
                onClick = { onClick(it) }
            )
        }

        ListOrientation.VERTICAL -> {
            VerticalList(
                songs = songs,
                iconShape = iconShape,
                gridSize = gridSize,
                onClick = { onClick(it) }
            )
        }
    }
}

@Composable
private fun HorizontalList(
    songs: List<Song>,
    iconShape: Shape,
    onClick: (song: Song) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .height(150.dp),
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) {
        items(items = songs) {
            AppItem(
                platform = it.platform,
                iconShape = iconShape,
                onClick = { onClick(it) }
            )
        }
    }
}

@Composable
private fun VerticalList(
    songs: List<Song>,
    iconShape: Shape,
    gridSize: Int,
    onClick: (song: Song) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(vertical = 15.dp),
        columns = GridCells.Fixed(gridSize),
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) {
        items(items = songs) {
            AppItem(
                platform = it.platform,
                iconShape = iconShape,
                onClick = { onClick(it) }
            )
        }
    }
}

@Composable
private fun AppItem(
    platform: String,
    iconShape: Shape,
    onClick: () -> Unit
) {
    val app = platforms[platform]!!
    val titleWords = app.title.split(' ')

    ClickableItem(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = app.icon),
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp)
                .clip(iconShape),
            contentDescription = app.title,
        )
        Text(text = titleWords[0])
        Text(text = if (titleWords.size > 1) titleWords[1] else "")
    }
}