package com.acszo.redomi.ui.common

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import com.acszo.redomi.ui.nav.initialOffset

enum class TransitionDirection(val value: Int) {
    FORWARD(1),
    BACKWARD(-1)
}

fun enterHorizontalTransition(
    direction: TransitionDirection
): EnterTransition = fadeIn(
        animationSpec = tween(
            durationMillis = 210,
            delayMillis = 90,
            easing = LinearOutSlowInEasing
        )
    ) + slideInHorizontally(
        animationSpec = tween(durationMillis = 300)
    ) {
        direction.value * (it * initialOffset).toInt()
    }


fun exitHorizontalTransition(
    direction: TransitionDirection
): ExitTransition = fadeOut(
        animationSpec = tween(
            durationMillis = 90,
            easing = FastOutLinearInEasing
        )
    ) + slideOutHorizontally(
        animationSpec = tween(durationMillis = 300)
    ) {
        direction.value * (it * initialOffset).toInt()
    }

fun enterVerticalTransition(): EnterTransition =
    slideInVertically(initialOffsetY = { -40 }) + fadeIn(initialAlpha = 0.3f)

fun exitVerticalTransition(): ExitTransition =
    slideOutVertically(targetOffsetY = { -40 }) + fadeOut(animationSpec = tween(200))