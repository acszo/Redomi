package com.acszo.redomi.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acszo.redomi.R
import com.acszo.redomi.BuildConfig
import com.acszo.redomi.data.DataStoreConst.HORIZONTAL_LIST
import com.acszo.redomi.data.DataStoreConst.SYSTEM_THEME
import com.acszo.redomi.data.DataStoreConst.getListType
import com.acszo.redomi.data.DataStoreConst.getThemeMode
import com.acszo.redomi.data.DataStoreConst.themes
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.component.PageTitle
import com.acszo.redomi.ui.component.RadioButtonItem
import com.acszo.redomi.ui.component.RedomiAlertDialog
import com.acszo.redomi.ui.component.SettingsItem
import com.acszo.redomi.ui.component.SmallTopAppBar
import com.acszo.redomi.ui.nav.Pages.appsPage
import com.acszo.redomi.ui.nav.Pages.layoutPage
import com.acszo.redomi.util.Clipboard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavController) {
    val pageTitle: String = stringResource(id = R.string.app_name)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dataStore = SettingsDataStore(context)
    val listType = dataStore.getLayoutListType.collectAsState(initial = HORIZONTAL_LIST)
    val themeMode = dataStore.getThemeMode.collectAsState(initial = SYSTEM_THEME)

    val uriHandle = LocalUriHandler.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val appVersion: String = BuildConfig.VERSION_NAME

    val openThemeDialog = remember { mutableStateOf(false)  }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            SmallTopAppBar(
                title = pageTitle,
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
                Modifier.padding(it),
        ) {
            item {
                PageTitle(
                    title = pageTitle,
                    scrollBehavior = scrollBehavior
                )
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.apps),
                    icon = R.drawable.grid_view_icon,
                    description = stringResource(id = R.string.apps_description)
                ) {
                    navController.navigate(appsPage)
                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.layout),
                    icon = R.drawable.format_list_bulleted_icon,
                    description = stringResource(id = getListType(listType.value!!))
                ) {
                    navController.navigate(layoutPage)
                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.theme),
                    icon = R.drawable.color_lens_filled_icon,
                    description = stringResource(id = getThemeMode(themeMode.value!!))
                ) {
                    openThemeDialog.value = true
                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.github),
                    icon = R.drawable.github_icon,
                    description = stringResource(id = R.string.github_description)
                ) {
                    uriHandle.openUri("https://github.com/acszo/Redomi")
                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.update),
                    icon = R.drawable.update_icon,
                    description = stringResource(id = R.string.update_description)
                ) {

                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.version),
                    icon = R.drawable.info_filled_icon,
                    description = appVersion
                ) {
                    Clipboard().copyText(
                        clipboardManager = clipboardManager,
                        text = appVersion
                    )
                }
            }
        }
    }

    if (openThemeDialog.value) {
        RedomiAlertDialog(
            icon =  R.drawable.color_lens_outline_icon,
            title = stringResource(id = R.string.theme),
            content = {
                themes.forEach { item ->
                    RadioButtonItem(
                        value = getThemeMode(themeMode.value!!),
                        text = item.value,
                        padding = 0.dp
                    ) {
                        scope.launch {
                            dataStore.saveThemeMode(item.key)
                        }
                    }
                }
            },
            confirmAction = { openThemeDialog.value = false }
        )
    }
}