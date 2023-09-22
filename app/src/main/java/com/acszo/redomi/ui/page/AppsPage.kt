package com.acszo.redomi.ui.page

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBarDefaults
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
import com.acszo.redomi.viewmodel.DataStoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsPage(
    dataStoreViewModel: DataStoreViewModel = hiltViewModel(),
    backButton: @Composable () -> Unit
) {
    val pageTitle: String = stringResource(id = R.string.apps)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val tabs: List<String> = listOf(
        stringResource(id = R.string.installed),
        stringResource(id = R.string.all)
    )
    val selectedTab = remember { mutableStateOf(tabs[0]) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
        title = pageTitle,
        description = stringResource(id = R.string.apps_description_page),
        scrollBehavior = scrollBehavior,
        backButton = { backButton() }
    ) { padding, titleWithDescription ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            item {
                titleWithDescription()
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
                        val title: String = app.title.replace("(?<=[^A-Z])(?=[A-Z])".toRegex(), " ")
                                .replaceFirstChar { it.uppercase() }
                        AppCheckBoxItem(
                            icon = app.icon,
                            title = title,
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
                        val title: String = app.title.replace("(?<=[^A-Z])(?=[A-Z])".toRegex(), " ")
                                .replaceFirstChar { it.uppercase() }
                        AppCheckBoxItem(
                            icon = app.icon,
                            title = title,
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

private fun isAppInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}