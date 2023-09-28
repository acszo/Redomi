package com.acszo.redomi.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun RotatingIcon(
    modifier: Modifier = Modifier,
    icon: Int,
    size: Dp,
    tint: Color = MaterialTheme.colorScheme.secondary,
    contentDescription: String? = null,
    startValue: Float,
    targetValue: Float,
    duration: Int,
    easing: Easing
) {
    val currentRotation by remember { mutableFloatStateOf(startValue) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(currentRotation) {
        rotation.animateTo(
            targetValue = targetValue,
            animationSpec = tween(duration, easing = easing),
        )
    }

    Icon(
        painter = painterResource(id = icon),
        modifier = modifier
            .size(size)
            .graphicsLayer { rotationZ = rotation.value },
        tint = tint,
        contentDescription = contentDescription
    )
}