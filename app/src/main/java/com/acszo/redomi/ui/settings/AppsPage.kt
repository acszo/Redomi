package com.acszo.redomi.ui.settings

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.acszo.redomi.R
import com.acszo.redomi.data.AllAppSerializable
import com.acszo.redomi.model.AllApps
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.ui.component.AppCheckBoxItem
import com.acszo.redomi.ui.component.PageBottomInfo
import com.acszo.redomi.ui.component.PageDescription
import com.acszo.redomi.ui.component.PageTitle
import com.acszo.redomi.ui.component.SmallTopAppBar
import com.acszo.redomi.ui.component.Tabs
import kotlinx.coroutines.launch

val Context.dataStore by dataStore("selected-apps", AllAppSerializable)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsPage(
    backButton: @Composable () -> Unit
) {
    val pageTitle: String = stringResource(id = R.string.apps)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val tabs: List<String> = listOf(
        stringResource(id = R.string.installed),
        stringResource(id = R.string.all)
    )
    val selectedTab = remember { mutableStateOf(tabs[0]) }
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val allApps = context.dataStore.data.collectAsState(initial = AllApps()).value.apps.toMutableList()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            SmallTopAppBar(
                title = pageTitle,
                scrollBehavior = scrollBehavior,
                navigationIcon = { backButton() }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            Column {
                PageTitle(
                    title = pageTitle,
                    scrollBehavior = scrollBehavior
                )
                PageDescription(description = stringResource(id = R.string.apps_description_page))
            }
            Tabs(
                tabs = tabs,
                selectedTab = selectedTab
            )
            Column {
                if (selectedTab.value == tabs.first()) {
                    val installedApps: List<AppDetails> = platforms.filter {
                        var isInstalled = false
                        for (packageName in it.packageName) {
                            if (isAppInstalled(context, packageName)) isInstalled = true
                        }
                        isInstalled
                    }
                    for (app in installedApps) {
                        val title: String = app.title.replace("(?<=[^A-Z])(?=[A-Z])".toRegex(), " ")
                            .replaceFirstChar { it.uppercase() }
                        AppCheckBoxItem(
                            icon = app.icon,
                            title = title,
                            isChecked = allApps.contains(app),
                            onCheckedAction = {
                                scope.launch {
                                    allApps.add(app)
                                    setAllApps(context, allApps.toList())
                                }
                            },
                            onUnCheckedAction = {
                                scope.launch {
                                    allApps.remove(app)
                                    setAllApps(context, allApps.toList())
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
                            isChecked = allApps.contains(app),
                            onCheckedAction = {
                                scope.launch {
                                    allApps.add(app)
                                    setAllApps(context, allApps.toList())
                                }
                            },
                            onUnCheckedAction = {
                                scope.launch {
                                    allApps.remove(app)
                                    setAllApps(context, allApps.toList())
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 28.dp)
            )
            PageBottomInfo(
                if (selectedTab.value == tabs[0])
                    stringResource(id = R.string.installed_tab_info)
                else
                    stringResource(id = R.string.all_tab_info)
            )
        }
    }
}

private suspend fun setAllApps(context: Context, allApps: List<AppDetails>) {
    context.dataStore.updateData {
        it.copy(apps = allApps)
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