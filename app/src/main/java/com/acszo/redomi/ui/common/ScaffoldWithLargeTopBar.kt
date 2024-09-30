package com.acszo.redomi.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithLargeTopAppBar(
    title: String,
    description: String? = null,
    backButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues, @Composable () -> Unit) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = title,
                scrollBehavior = scrollBehavior,
                navigationIcon = { backButton() }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        content(it.addHorizontalPadding(28.dp).addNavigationBarsPadding()) {
            Column {
                PageTitle(
                    title = title,
                    scrollBehavior = scrollBehavior
                )
                if (description != null) {
                    PageDescription(description = description)
                }
            }
        }
    }
}

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
                modifier = Modifier.graphicsLayer { alpha = scrollBehavior.state.overlappedFraction },
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = { navigationIcon() }
    )
}

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
            .padding(vertical = 28.dp)
            .graphicsLayer { alpha = (1f - scrollBehavior.state.overlappedFraction) },
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.displaySmall,
    )
}

@Composable
fun PageDescription(
    description: String
) {
    Text(
        text = description,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}