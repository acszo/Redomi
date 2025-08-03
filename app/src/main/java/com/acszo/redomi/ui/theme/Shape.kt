package com.acszo.redomi.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

val topItemShape: RoundedCornerShape
    @Composable get () = RoundedCornerShape(
        topStart = MaterialTheme.shapes.large.topStart,
        topEnd = MaterialTheme.shapes.large.topEnd,
        bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
        bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
    )

val middleItemShape: RoundedCornerShape
    @Composable get() = RoundedCornerShape(
        corner = MaterialTheme.shapes.extraSmall.topStart
    )

val bottomItemShape: RoundedCornerShape
    @Composable get () = RoundedCornerShape(
        topStart = MaterialTheme.shapes.extraSmall.topStart,
        topEnd = MaterialTheme.shapes.extraSmall.topEnd,
        bottomStart = MaterialTheme.shapes.large.bottomStart,
        bottomEnd = MaterialTheme.shapes.large.bottomEnd
    )