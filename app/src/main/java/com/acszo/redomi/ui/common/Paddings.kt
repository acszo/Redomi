package com.acszo.redomi.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun PaddingValues.addHorizontalPadding(
    padding: Dp
): PaddingValues = PaddingValues(
    start = padding,
    top = this.calculateTopPadding(),
    end = padding,
    bottom = this.calculateBottomPadding()
)

@Composable
fun PaddingValues.addNavigationBarsPadding(): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = this.calculateStartPadding(layoutDirection),
        top = this.calculateTopPadding(),
        end = this.calculateEndPadding(layoutDirection),
        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    )
}

@Composable
fun PaddingValues.removeTopPadding(): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = this.calculateStartPadding(layoutDirection),
        top = 0.dp,
        end = this.calculateEndPadding(layoutDirection),
        bottom = this.calculateBottomPadding()
    )
}