package com.acszo.redomi.ui.page.settings.apps.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acszo.redomi.data.AppList
import com.acszo.redomi.ui.common.selectedBoxColor
import com.acszo.redomi.ui.common.selectedTextColor

@Composable
fun Tabs(
    selectedTab: AppList,
    onClick: (AppList) -> Unit
) {
    val width = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppList.entries.forEach { tab ->
            val tabColor by animateColorAsState(
                targetValue = selectedBoxColor(selectedTab == tab),
                animationSpec = tween(100, 0, LinearEasing),
                label = ""
            )

            val animatedPadding by animateDpAsState(
                targetValue = if (selectedTab == tab) 0.dp else width.value / 8,
                label = ""
            )

            // goofy double clipping 🔥🔥🔥
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onGloballyPositioned {
                        width.value = with(density) {
                            it.size.width.toDp()
                        }
                    }
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable { onClick(tab) }
                    .clip(MaterialTheme.shapes.extraLarge)
                    .layout { measurable, constraints ->
                        val size = (width.value - animatedPadding).toPx()
                        val placeable = measurable.measure(
                            constraints.copy(
                                minWidth = size.toInt(),
                                maxWidth = size.toInt()
                            )
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    }
                    .drawBehind { drawRect(tabColor) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = tab.toRes),
                    modifier = Modifier.padding(vertical = 15.dp),
                    maxLines = 1,
                    color = selectedTextColor(selectedTab == tab),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500,
                    )
                )
            }
        }
    }
}