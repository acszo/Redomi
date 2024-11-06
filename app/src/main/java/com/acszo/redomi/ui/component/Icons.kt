package com.acszo.redomi.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R

@Composable
fun RotatingIcon(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    size: Dp,
    tint: Color = MaterialTheme.colorScheme.secondary,
    startValue: Float,
    contentDescription: String? = null
) {
    val rotation = remember { Animatable(startValue) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 180f,
            animationSpec = tween(300, easing = LinearOutSlowInEasing),
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

@Composable
fun NewReleaseIcon() {
    val context = LocalContext.current
    val display = context.resources.displayMetrics
    val width = display.widthPixels.dp / display.density
    val height = display.heightPixels.dp / display.density
    val widthOffset = width / 1.5f
    val heightOffset = height / 2.2f

    RotatingIcon(
        icon = R.drawable.ic_new_releases_outside,
        modifier = Modifier.offset(width - widthOffset, height - heightOffset),
        size = width,
        tint = MaterialTheme.colorScheme.secondaryContainer,
        startValue = 90f,
    )

    Icon(
        painter = painterResource(id = R.drawable.ic_new_releases_inside),
        modifier = Modifier
            .size(width)
            .offset(width - widthOffset, height - heightOffset),
        tint = MaterialTheme.colorScheme.secondaryContainer,
        contentDescription = null,
    )
}