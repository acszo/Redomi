package com.acszo.redomi.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.R
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.ui.component.AppCheckBoxItem
import com.acszo.redomi.ui.component.Tabs
import com.acszo.redomi.ui.component.common_page.PageBottomInfo
import com.acszo.redomi.ui.component.common_page.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.utils.StringUtil.separateUppercase
import com.acszo.redomi.viewmodel.DataStoreViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsPage(
    dataStoreViewModel: DataStoreViewModel,
    backButton: @Composable () -> Unit
) {
    val tabs: List<String> = listOf(
        stringResource(id = R.string.installed),
        stringResource(id = R.string.all)
    )
    val selectedTab = remember { mutableStateOf(tabs[0]) }

    LaunchedEffect(Unit) {
        dataStoreViewModel.getInstalledApps()
        dataStoreViewModel.getAllApps()
    }

    val installedApps = dataStoreViewModel.installedApps.collectAsStateWithLifecycle().value.toMutableList()
    val allAppsSelection = dataStoreViewModel.allApps.collectAsStateWithLifecycle().value.toMutableList()

    ScaffoldWithLargeTopAppBar(
        title = stringResource(id = R.string.apps),
        description = stringResource(id = R.string.apps_description_page),
        backButton = { backButton() }
    ) { padding, pageTitleWithDescription ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            item {
                pageTitleWithDescription()
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
            }

            stickyHeader {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(14.dp),
                ) {
                    Tabs(
                        tabs = tabs,
                        selectedTab = selectedTab
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
            }

            if (selectedTab.value == tabs.first()) {
                items(platforms) { app ->
                    AppCheckBoxItem(
                        icon = app.icon,
                        title = separateUppercase(app.title),
                        size = installedApps.size,
                        isChecked = installedApps.contains(app),
                        onCheckedAction = {
                            installedApps.add(app)
                            dataStoreViewModel.setInstalledApps(installedApps.toList())
                        },
                        onUnCheckedAction = {
                            installedApps.remove(app)
                            dataStoreViewModel.setInstalledApps(installedApps.toList())
                        }
                    )
                }
            } else {
                items(platforms) { app ->
                    AppCheckBoxItem(
                        icon = app.icon,
                        title = separateUppercase(app.title),
                        size = allAppsSelection.size,
                        isChecked = allAppsSelection.contains(app),
                        onCheckedAction = {
                            allAppsSelection.add(app)
                            dataStoreViewModel.setAllApps(allAppsSelection.toList())
                        },
                        onUnCheckedAction = {
                            allAppsSelection.remove(app)
                            dataStoreViewModel.setAllApps(allAppsSelection.toList())
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 28.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                PageBottomInfo(
                    if (selectedTab.value == tabs[0])
                        stringResource(id = R.string.installed_tab_info)
                    else
                        stringResource(id = R.string.all_tab_info)
                )
            }
        }
    }
}