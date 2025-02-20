package com.acszo.redomi.ui.page.bottom_sheet

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun BottomSheetError(
    message: String
) {
    val animatedIcon = AnimatedImageVector.animatedVectorResource(R.drawable.anim_ic_not_found)
    var atEnd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        atEnd = true
    }

    Column(
        modifier = Modifier.size(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Icon(
            painter = rememberAnimatedVectorPainter(animatedIcon, atEnd),
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = message,
        )

        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}