package com.acszo.redomi.ui.page

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    val context = LocalContext.current
    val display = context.resources.displayMetrics
    val width = display.widthPixels.dp / display.density
    val height = display.heightPixels.dp / display.density
    val widthOffset = width / 1.5f
    val heightOffset = width / 1.1f

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
        if (isNotLatest) {
            val currentRotation by remember { mutableFloatStateOf(0f) }
            val rotation = remember { Animatable(currentRotation) }

            LaunchedEffect(currentRotation) {
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(180000, easing = LinearEasing)
                    ),
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.out_new_releases_icon),
                modifier = Modifier
                    .size(width)
                    .offset(width - widthOffset, height - heightOffset)
                    .graphicsLayer { rotationZ = rotation.value },
                tint = MaterialTheme.colorScheme.secondaryContainer,
                contentDescription = null
            )

            Icon(
                painter = painterResource(id = R.drawable.in_new_releases_icon),
                modifier = Modifier
                    .size(width)
                    .offset(width - widthOffset, height - heightOffset),
                tint = MaterialTheme.colorScheme.secondaryContainer,
                contentDescription = null
            )
        }

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
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                onClick = {
                    if (context.packageManager.canRequestPackageInstalls()) {
                        // TODO
                    } else {
                        context.startActivity(
                            Intent(
                                android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                Uri.parse("package:${context.packageName}")
                            )
                        )
                    }
                }
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