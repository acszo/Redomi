package com.acszo.redomi.ui.component

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
import androidx.compose.runtime.MutableState
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
import com.acszo.redomi.model.AppList

@Composable
fun Tabs(
    selectedTab: MutableState<AppList>
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
            val selectedTabColor = animateColorAsState(
                targetValue = if (selectedTab.value == tab)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant,
                animationSpec = tween(100, 0, LinearEasing),
                label = ""
            )

            val padding = animateDpAsState(
                targetValue = if (selectedTab.value == tab) 0.dp else width.value / 8,
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
                    .clickable {
                        selectedTab.value = tab
                    }
                    .clip(MaterialTheme.shapes.extraLarge)
                    .layout { measurable, constraints ->
                        val size = width.value.toPx() - padding.value.toPx()
                        val placeable = measurable.measure(
                            constraints.copy(minWidth = size.toInt())
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    }
                    .drawBehind { drawRect(selectedTabColor.value) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = tab.toRes),
                    modifier = Modifier.padding(vertical = 15.dp),
                    maxLines = 1,
                    color = if (selectedTab.value == tab) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500,
                    )
                )
            }
        }
    }
}