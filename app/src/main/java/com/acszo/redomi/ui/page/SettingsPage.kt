package com.acszo.redomi.ui.page

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.acszo.redomi.BuildConfig
import com.acszo.redomi.R
import com.acszo.redomi.data.DataStoreConst.listTypes
import com.acszo.redomi.data.DataStoreConst.themes
import com.acszo.redomi.ui.component.IconItemDialog
import com.acszo.redomi.ui.component.RadioButtonItem
import com.acszo.redomi.ui.component.RedomiAlertDialog
import com.acszo.redomi.ui.component.SettingsItem
import com.acszo.redomi.ui.component.common_page.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.ui.nav.Pages.appsPage
import com.acszo.redomi.ui.nav.Pages.layoutPage
import com.acszo.redomi.ui.nav.Pages.updatePage
import com.acszo.redomi.utils.ClipboardUtils.copyText
import com.acszo.redomi.utils.IntentUtil.onIntentOpenDefaultsApp
import com.acszo.redomi.viewmodel.DataStoreViewModel

@Composable
fun SettingsPage(
    dataStoreViewModel: DataStoreViewModel,
    navController: NavController,
    isUpdateAvailable: Boolean
) {
    val context = LocalContext.current

    val isFirstTime by dataStoreViewModel.isFirstTime.collectAsStateWithLifecycle()
    val currentListType by dataStoreViewModel.layoutListType.collectAsStateWithLifecycle()
    val currentThemeMode by dataStoreViewModel.themeMode.collectAsStateWithLifecycle()

    val uriHandle = LocalUriHandler.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val versionName = BuildConfig.VERSION_NAME
    val appVersion = "Version: $versionName"
    val modelName = "Model: ${Build.MODEL}"
    val androidVersion = "Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val openThemeDialog = remember { mutableStateOf(false) }

    ScaffoldWithLargeTopAppBar(
        title = stringResource(id = R.string.app_name)
    ) { padding, pageTitle ->
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            item {
                pageTitle()
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.apps),
                    icon = R.drawable.ic_grid_view,
                    description = stringResource(id = R.string.apps_description)
                ) {
                    navController.navigate(appsPage)
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.layout),
                    icon = R.drawable.ic_format_list_bulleted,
                    description = stringResource(id = listTypes[currentListType]!!)
                ) {
                    navController.navigate(layoutPage)
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.theme),
                    icon = R.drawable.ic_color_lens_filled,
                    description = stringResource(id = themes[currentThemeMode]!!)
                ) {
                    openThemeDialog.value = true
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.update),
                    icon = R.drawable.ic_update,
                    description = stringResource(id = R.string.update_description),
                    isAlertIconVisible = isUpdateAvailable
                ) {
                    navController.navigate(updatePage)
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.github),
                    icon = R.drawable.ic_github,
                    description = stringResource(id = R.string.github_description)
                ) {
                    uriHandle.openUri("https://github.com/acszo/Redomi")
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.version),
                    icon = R.drawable.ic_info_filled,
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

    if (isFirstTime!!) {
        RedomiAlertDialog(
            icon =  R.drawable.ic_description,
            title = stringResource(id = R.string.dialog_setup_title),
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.dialog_setup_description))
                    IconItemDialog(
                        icon = R.drawable.ic_done_all,
                        description = stringResource(id = R.string.dialog_setup_description_checked)
                    )
                    IconItemDialog(
                        icon = R.drawable.ic_remove_done,
                        description = stringResource(id = R.string.dialog_setup_description_unchecked)
                    )
                }
            },
            onConfirmAction = {
                onIntentOpenDefaultsApp(context)
                dataStoreViewModel.setIsFirstTime()
            }
        )
    }

    if (openThemeDialog.value) {
        RedomiAlertDialog(
            icon =  R.drawable.ic_color_lens_outline,
            title = stringResource(id = R.string.theme),
            content = {
                themes.forEach { item ->
                    RadioButtonItem(
                        modifier = Modifier.padding(start = 15.dp),
                        value = themes[currentThemeMode]!!,
                        text = item.value,
                        horizontalPadding = 0.dp
                    ) {
                        dataStoreViewModel.setThemeMode(item.key)
                    }
                }
            },
            onDismissRequest = { openThemeDialog.value = false },
            onConfirmAction = { openThemeDialog.value = false }
        )
    }
}