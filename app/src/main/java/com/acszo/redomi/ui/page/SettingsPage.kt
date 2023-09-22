package com.acszo.redomi.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.acszo.redomi.ui.component.common_page.PageTitle
import com.acszo.redomi.ui.component.RadioButtonItem
import com.acszo.redomi.ui.component.RedomiAlertDialog
import com.acszo.redomi.ui.component.SettingsItem
import com.acszo.redomi.ui.component.common_page.ScaffoldWithTopAppBar
import com.acszo.redomi.ui.nav.Pages.appsPage
import com.acszo.redomi.ui.nav.Pages.layoutPage
import com.acszo.redomi.ui.nav.Pages.updatePage
import com.acszo.redomi.utils.ClipboardUtils.copyText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    navController: NavController,
    isUpdateAvailable: Boolean
) {
    val pageTitle: String = stringResource(id = R.string.app_name)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dataStore = SettingsDataStore(context)
    val isFirstTime = dataStore.getIsFirstTime.collectAsState(initial = false)
    val listType = dataStore.getLayoutListType.collectAsState(initial = HORIZONTAL_LIST)
    val themeMode = dataStore.getThemeMode.collectAsState(initial = SYSTEM_THEME)

    val uriHandle = LocalUriHandler.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val versionName = BuildConfig.VERSION_NAME
    val appVersion = "Version: $versionName"
    val modelName = "Model: ${Build.MODEL}"
    val androidVersion = "Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val openThemeDialog = remember { mutableStateOf(false) }

    ScaffoldWithTopAppBar(
        title = pageTitle,
        scrollBehavior = scrollBehavior
    ) { padding, _ ->
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
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
                    description = stringResource(id = R.string.update_description),
                    isAlertIconVisible = isUpdateAvailable
                ) {
                    navController.navigate(updatePage)
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.version),
                    icon = R.drawable.info_filled_icon,
                    description = versionName
                ) {
                    copyText(
                        clipboardManager = clipboardManager,
                        text = appVersion + '\n' + modelName + '\n' + androidVersion
                    )
                }
            }
        }
    }

    if (isFirstTime.value!!) {
        RedomiAlertDialog(
            icon =  R.drawable.outline_new_releases_icon,
            title = stringResource(id = R.string.dialog_setup_title),
            content = {
                Text(text = stringResource(id = R.string.dialog_setup_description))
            },
            confirmAction = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                            Uri.parse("package:${context.packageName}")
                        )
                    )
                } else {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:${context.packageName}")
                        )
                    )
                }
                scope.launch {
                    dataStore.saveIsFirstTime(false)
                }
            }
        )
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