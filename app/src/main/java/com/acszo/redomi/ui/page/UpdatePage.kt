package com.acszo.redomi.ui.page

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.BuildConfig
import com.acszo.redomi.R
import com.acszo.redomi.model.DownloadStatus
import com.acszo.redomi.ui.component.RotatingIcon
import com.acszo.redomi.ui.component.common_page.ScaffoldWithTopAppBar
import com.acszo.redomi.utils.UpdateUtil.getApk
import com.acszo.redomi.utils.UpdateUtil.installApk
import com.acszo.redomi.viewmodel.UpdateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UpdatePage(
    updateViewModel: UpdateViewModel = hiltViewModel(),
    backButton: @Composable () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val latestRelease by updateViewModel.latestRelease.collectAsStateWithLifecycle()
    val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()
    val isLoading by updateViewModel.isLoading.collectAsStateWithLifecycle()
    val progressDownloadStatus = remember { mutableStateOf(DownloadStatus.Finished as DownloadStatus) }

    val display = context.resources.displayMetrics
    val width = display.widthPixels.dp / display.density
    val height = display.heightPixels.dp / display.density
    val widthOffset = width / 1.5f
    val heightOffset = width / 1.1f

    ScaffoldWithTopAppBar(
        title = stringResource(id = R.string.update),
        description = stringResource(id = R.string.update_description_page),
        backButton = { backButton() },
    ) { padding, pageTitleWithDescription ->
        if (isUpdateAvailable) {
            RotatingIcon(
                modifier = Modifier.offset(width - widthOffset, height - heightOffset),
                icon = R.drawable.out_new_releases_icon,
                size = width,
                tint = MaterialTheme.colorScheme.secondaryContainer,
                startValue = 0f,
                targetValue = 360f,
                duration = 180000,
                easing = LinearEasing,
            )

            Icon(
                painter = painterResource(id = R.drawable.in_new_releases_icon),
                modifier = Modifier
                    .size(width)
                    .offset(width - widthOffset, height - heightOffset),
                tint = MaterialTheme.colorScheme.secondaryContainer,
                contentDescription = null,
            )
        }

        Column(
            modifier = Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .weight(1f)
                    .fadingEdge(),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            ) {
                item {
                    pageTitleWithDescription()
                }

                if (latestRelease != null) {
                    item {
                        Text(
                            text = if (isUpdateAvailable) stringResource(id = R.string.title_changelogs_new) else stringResource(id = R.string.title_changelogs_current),
                            modifier = Modifier.padding(horizontal = 28.dp),
                            color = if (isUpdateAvailable) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    item {
                        Text(
                            text = latestRelease!!.name,
                            modifier = Modifier.padding(horizontal = 28.dp),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }

                    item {
                        Text(
                            text = latestRelease!!.body,
                            modifier = Modifier.padding(horizontal = 38.dp),
                        )
                    }
                }
            }

            if (!isLoading && latestRelease == null) {
                Box(
                    modifier = Modifier.weight(2f).fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.update_info_failed),
                        modifier = Modifier.padding(horizontal = 28.dp),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                contentPadding = PaddingValues(0.dp),
                enabled = progressDownloadStatus.value !is DownloadStatus.Downloading,
                onClick = {
                    if (isUpdateAvailable) {
                        scope.launch(Dispatchers.IO) {
                            if (!context.getApk().exists()) {
                                updateViewModel.downloadApk(context, latestRelease!!)
                                    .collect { downloadStatus ->
                                        progressDownloadStatus.value = downloadStatus
                                        if (downloadStatus is DownloadStatus.Finished) {
                                            installApk(context)
                                        }
                                    }
                            } else {
                                 installApk(context)
                            }
                        }
                    } else {
                        updateViewModel.checkUpdate(BuildConfig.VERSION_NAME)
                    }
                },
            ) {
                if (progressDownloadStatus.value is DownloadStatus.Downloading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LinearProgressIndicator(
                            progress = (progressDownloadStatus.value as DownloadStatus.Downloading).progress.toFloat() / 100,
                            modifier = Modifier.fillMaxSize(),
                        )

                        Text(
                            text = (progressDownloadStatus.value as DownloadStatus.Downloading).progress.toString() + " %",
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }

                Text(
                    text = if (isUpdateAvailable) stringResource(id = R.string.do_update)
                    else stringResource(id = R.string.check_updates),
                )
            }
        }
    }
}

private fun Modifier.fadingEdge() = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(0.95f to Color.Red, 1f to Color.Transparent),
            blendMode = BlendMode.DstIn
        )
    }