package com.acszo.redomi.ui.page.settings.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.R
import com.acszo.redomi.model.DownloadStatus
import com.acszo.redomi.ui.common.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.ui.common.NewReleaseIcon
import com.acszo.redomi.ui.common.fadingEdge
import com.acszo.redomi.ui.common.ignoreHorizontalPadding
import com.acszo.redomi.utils.UpdateUtil.getApk
import com.acszo.redomi.utils.UpdateUtil.installApk
import com.acszo.redomi.versionName
import com.acszo.redomi.viewmodel.UpdateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UpdatePage(
    updateViewModel: UpdateViewModel,
    backButton: @Composable () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val uiState by updateViewModel.updatePageUiState.collectAsStateWithLifecycle()
    val isUpdateAvailable by updateViewModel.isUpdateAvailable.collectAsStateWithLifecycle()
    val progressDownloadStatus = remember { mutableStateOf(DownloadStatus.Finished as DownloadStatus) }

    ScaffoldWithLargeTopAppBar(
        title = stringResource(id = R.string.update),
        description = stringResource(id = R.string.update_description_page),
        backButton = { backButton() }
    ) { padding, pageTitleWithDescription ->
        if (isUpdateAvailable) {
            NewReleaseIcon()
        }

        Column(
            modifier = Modifier.padding(padding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fadingEdge(),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                contentPadding = PaddingValues(bottom = 10.dp),
            ) {
                item {
                    pageTitleWithDescription()
                }

                if (uiState.latestRelease != null) {
                    item {
                        Text(
                            text = if (isUpdateAvailable) stringResource(id = R.string.title_changelogs_new) else stringResource(id = R.string.title_changelogs_current),
                            color = if (isUpdateAvailable) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    item {
                        Text(
                            text = uiState.latestRelease?.name ?: "",
                            style = MaterialTheme.typography.displaySmall,
                        )
                    }

                    item {
                        AnnotatedString(string = uiState.latestRelease?.body ?: "")
                    }
                }
            }

            if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = uiState.error!!),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ignoreHorizontalPadding()
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(vertical = 16.dp),
                contentPadding = PaddingValues(0.dp),
                enabled = progressDownloadStatus.value !is DownloadStatus.Downloading,
                onClick = {
                    if (isUpdateAvailable) {
                        scope.launch(Dispatchers.IO) {
                            if (!context.getApk().exists()) {
                                updateViewModel.downloadApk(context, uiState.latestRelease!!)
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
                        updateViewModel.checkUpdate(versionName)
                    }
                },
            ) {
                if (progressDownloadStatus.value is DownloadStatus.Downloading) {
                    val progress = (progressDownloadStatus.value as DownloadStatus.Downloading).progress
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LinearProgressIndicator(
                            progress = { progress.toFloat() / 100 },
                            modifier = Modifier.fillMaxSize(),
                        )

                        Text(
                            text = "$progress %",
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }

                Text(
                    text = stringResource(
                        id = if (isUpdateAvailable) R.string.do_update else R.string.check_updates
                    ),
                )
            }
        }
    }
}