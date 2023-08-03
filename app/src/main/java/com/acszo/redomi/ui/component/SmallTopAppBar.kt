package com.acszo.redomi.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    navigationIcon: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = scrollBehavior.state.overlappedFraction
                ),
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = { navigationIcon() }
    )
}