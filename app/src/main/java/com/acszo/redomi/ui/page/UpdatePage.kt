package com.acszo.redomi.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acszo.redomi.R
import com.acszo.redomi.ui.component.common_page.PageDescription
import com.acszo.redomi.ui.component.common_page.PageTitle
import com.acszo.redomi.ui.component.common_page.SmallTopAppBar
import com.acszo.redomi.viewmodel.GithubViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePage(
    githubViewModel: GithubViewModel = hiltViewModel(),
    backButton: @Composable () -> Unit
) {
    val pageTitle: String = stringResource(id = R.string.update)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val latestRelease = githubViewModel.latestRelease.collectAsState().value
    val isNotLatest = githubViewModel.isNotLatest.collectAsState().value

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = pageTitle,
                scrollBehavior = scrollBehavior,
                navigationIcon = { backButton() }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        Column(
            modifier = Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .weight(1f)
                    .fadingEdge(),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                contentPadding = WindowInsets.navigationBars.asPaddingValues()
            ) {
                item {
                    Column {
                        PageTitle(
                            title = pageTitle,
                            scrollBehavior = scrollBehavior
                        )
                        PageDescription(description = stringResource(id = R.string.update_description_page))
                    }
                }
                item {
                    Text(
                        text = if (isNotLatest) stringResource(id = R.string.title_changelogs_new) else stringResource(id = R.string.title_changelogs_current),
                        modifier = Modifier.padding(horizontal = 28.dp),
                        color = if (isNotLatest) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                item {
                    Text(
                        text = latestRelease?.name ?: "",
                        modifier = Modifier.padding(horizontal = 28.dp),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                item {
                    Text(
                        text = latestRelease?.body ?: "",
                        modifier = Modifier.padding(horizontal = 38.dp),
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 10.dp),
                onClick = {  }
            ) {
                Text(
                    text = if (isNotLatest) stringResource(id = R.string.do_update)
                    else stringResource(id = R.string.check_updates)
                )
            }
        }
    }
}

fun Modifier.fadingEdge() = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(0.95f to Color.Red, 1f to Color.Transparent),
            blendMode = BlendMode.DstIn
        )
    }