package com.acszo.redomi.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.R
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.model.AppList
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.ui.common.PageBottomInfo
import com.acszo.redomi.ui.common.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.ui.common.removeTopPadding
import com.acszo.redomi.ui.component.AppCheckBoxItem
import com.acszo.redomi.ui.component.Tabs
import com.acszo.redomi.utils.StringUtil.separateUppercase
import com.acszo.redomi.viewmodel.DataStoreViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsPage(
    dataStoreViewModel: DataStoreViewModel,
    backButton: @Composable () -> Unit
) {
    val selectedTab = remember { mutableStateOf(AppList.OPENING) }

    LaunchedEffect(Unit) {
        dataStoreViewModel.getOpeningApps()
        dataStoreViewModel.getSharingApps()
    }

    val iconShape by dataStoreViewModel.iconShape.collectAsStateWithLifecycle()
    val openingApps = dataStoreViewModel.openingApps.collectAsStateWithLifecycle().value.toMutableList()
    val sharingApps = dataStoreViewModel.sharingApps.collectAsStateWithLifecycle().value.toMutableList()

    ScaffoldWithLargeTopAppBar(
        title = stringResource(id = R.string.apps),
        description = stringResource(id = R.string.apps_description_page),
        backButton = { backButton() }
    ) { padding, pageTitleWithDescription ->
        LazyColumn(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize(),
            contentPadding = padding.removeTopPadding(),
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
                        .padding(vertical = 14.dp),
                ) {
                    Tabs(selectedTab = selectedTab)
                }
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
            }

            if (selectedTab.value == AppList.OPENING) {
                items(platforms) { app ->
                    AppCheckBoxItem(
                        icon = app.icon,
                        iconShape = IconShape.entries[iconShape].shape,
                        title = separateUppercase(app.title),
                        size = openingApps.size,
                        isChecked = openingApps.contains(app),
                        onCheckedAction = {
                            openingApps.add(app)
                            dataStoreViewModel.setOpeningApps(openingApps.toList())
                        },
                        onUnCheckedAction = {
                            openingApps.remove(app)
                            dataStoreViewModel.setOpeningApps(openingApps.toList())
                        }
                    )
                }
            } else {
                items(platforms) { app ->
                    AppCheckBoxItem(
                        icon = app.icon,
                        iconShape = IconShape.entries[iconShape].shape,
                        title = separateUppercase(app.title),
                        size = sharingApps.size,
                        isChecked = sharingApps.contains(app),
                        onCheckedAction = {
                            sharingApps.add(app)
                            dataStoreViewModel.setSharingApps(sharingApps.toList())
                        },
                        onUnCheckedAction = {
                            sharingApps.remove(app)
                            dataStoreViewModel.setSharingApps(sharingApps.toList())
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                HorizontalDivider()
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                PageBottomInfo(
                    if (selectedTab.value == AppList.OPENING)
                        stringResource(id = R.string.opening_apps_tab_info)
                    else
                        stringResource(id = R.string.sharing_apps_tab_info)
                )
            }
        }
    }
}