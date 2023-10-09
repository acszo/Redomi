package com.acszo.redomi.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acszo.redomi.R
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.ui.component.AppCheckBoxItem
import com.acszo.redomi.ui.component.common_page.PageBottomInfo
import com.acszo.redomi.ui.component.Tabs
import com.acszo.redomi.ui.component.common_page.ScaffoldWithTopAppBar
import com.acszo.redomi.utils.UpdateUtil.isAppInstalled
import com.acszo.redomi.utils.StringUtil.separateUppercase
import com.acszo.redomi.viewmodel.DataStoreViewModel
import kotlinx.coroutines.launch

@Composable
fun AppsPage(
    dataStoreViewModel: DataStoreViewModel = hiltViewModel(),
    backButton: @Composable () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val tabs: List<String> = listOf(
        stringResource(id = R.string.installed),
        stringResource(id = R.string.all)
    )
    val selectedTab = remember { mutableStateOf(tabs[0]) }

    LaunchedEffect(Unit) {
        dataStoreViewModel.getInstalledApps()
        dataStoreViewModel.getAllApps()
    }

    val installedApps = dataStoreViewModel.installedApps.collectAsState().value.toMutableList()
    val allApps = dataStoreViewModel.allApps.collectAsState().value.toMutableList()

    val checkInstalled: List<AppDetails> = platforms.filter {
        var isInstalled = false
        for (packageName in it.packageName) {
            if (isAppInstalled(context, packageName)) isInstalled = true
        }
        isInstalled
    }
    val checkInstalledSize = installedApps.filter { it in checkInstalled }.size

    ScaffoldWithTopAppBar(
        title = stringResource(id = R.string.apps),
        description = stringResource(id = R.string.apps_description_page),
        backButton = { backButton() }
    ) { padding, pageTitleWithDescription ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            item {
                pageTitleWithDescription()
            }

            item {
                Tabs(
                    tabs = tabs,
                    selectedTab = selectedTab
                )
            }

            item {
                if (selectedTab.value == tabs.first()) {
                    for (app in checkInstalled) {
                        AppCheckBoxItem(
                            icon = app.icon,
                            title = separateUppercase(app.title),
                            size = checkInstalledSize,
                            isChecked = installedApps.contains(app),
                            onCheckedAction = {
                                scope.launch {
                                    installedApps.add(app)
                                    dataStoreViewModel.setInstalledApps(installedApps.toList())
                                }
                            },
                            onUnCheckedAction = {
                                scope.launch {
                                    installedApps.remove(app)
                                    dataStoreViewModel.setInstalledApps(installedApps.toList())
                                }
                            }
                        )
                    }
                } else {
                    for (app in platforms) {
                        AppCheckBoxItem(
                            icon = app.icon,
                            title = separateUppercase(app.title),
                            size = allApps.size,
                            isChecked = allApps.contains(app),
                            onCheckedAction = {
                                scope.launch {
                                    allApps.add(app)
                                    dataStoreViewModel.setAllApps(allApps.toList())
                                }
                            },
                            onUnCheckedAction = {
                                scope.launch {
                                    allApps.remove(app)
                                    dataStoreViewModel.setAllApps(allApps.toList())
                                }
                            }
                        )
                    }
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 28.dp)
                )
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