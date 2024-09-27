package com.acszo.redomi.ui.bottom_sheet

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.ui.component.ClickableItem
import com.acszo.redomi.utils.IntentUtil

@Composable
fun HorizontalList(
    iconShape: Shape,
    platforms: Map<AppDetails, String>,
    isActionSend: Boolean,
    showActionsMenu: MutableState<Boolean>,
    selectedPlatformLink: MutableState<String>
) {
    LazyRow(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .height(150.dp),
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) {
        items(items = platforms.toList()) { (app, link) ->
            AppItem(
                app = app,
                iconShape = iconShape,
                link = link,
                isActionSend = isActionSend,
                showActionsMenu = showActionsMenu,
                selectedPlatformLink = selectedPlatformLink,
            )
        }
    }
}

@Composable
fun VerticalList(
    gridSize: Int,
    iconShape: Shape,
    platforms: Map<AppDetails, String>,
    isActionSend: Boolean,
    showActionsMenu: MutableState<Boolean>,
    selectedPlatformLink: MutableState<String>
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(vertical = 15.dp),
        columns = GridCells.Fixed(gridSize),
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) {
        items(items = platforms.toList()) { (app, link) ->
            AppItem(
                app = app,
                iconShape = iconShape,
                link = link,
                isActionSend = isActionSend,
                showActionsMenu = showActionsMenu,
                selectedPlatformLink = selectedPlatformLink,
            )
        }
    }
}

@Composable
fun AppItem(
    app: AppDetails,
    iconShape: Shape,
    link: String,
    isActionSend: Boolean,
    showActionsMenu: MutableState<Boolean>,
    selectedPlatformLink: MutableState<String>
) {
    val context = LocalContext.current
    val titleWords: List<String> = splitSpaceToWords(app.title)

    ClickableItem(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable {
                if (!isActionSend) {
                    IntentUtil.onIntentView(context, link)
                } else {
                    showActionsMenu.value = true
                    selectedPlatformLink.value = link
                }
            }
    ) {
        Image(
            painterResource(id = app.icon),
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

private fun splitSpaceToWords(text: String): List<String> {
    return text.split(' ')
}