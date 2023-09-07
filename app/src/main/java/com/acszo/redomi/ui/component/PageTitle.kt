package com.acszo.redomi.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTitle(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(top = 18.dp)
            .padding(horizontal = 28.dp, vertical = 28.dp)
            .graphicsLayer { alpha = (1f - scrollBehavior.state.overlappedFraction) },
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSurface,
    )
}