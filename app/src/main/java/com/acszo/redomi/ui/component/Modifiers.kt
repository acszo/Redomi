package com.acszo.redomi.ui.component

import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

fun Modifier.fadingEdge() = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(0.95f to Color.Red, 1f to Color.Transparent),
            blendMode = BlendMode.DstIn
        )
    }

@Composable
fun Modifier.ignoreHorizontalPadding() = this
    .requiredWidth(LocalConfiguration.current.screenWidthDp.dp)