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
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.data.ListType
import com.acszo.redomi.data.Theme
import com.acszo.redomi.ui.common.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.ui.component.DefaultDialog
import com.acszo.redomi.ui.component.IconItemDialog
import com.acszo.redomi.ui.component.RadioButtonItem
import com.acszo.redomi.ui.component.SettingsItem
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
    val listType by dataStoreViewModel.layoutListType.collectAsStateWithLifecycle()
    val iconShape by dataStoreViewModel.iconShape.collectAsStateWithLifecycle()
    val themeMode by dataStoreViewModel.themeMode.collectAsStateWithLifecycle()

    val redomi = stringResource(id = R.string.app_name)

    val uriHandle = LocalUriHandler.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val versionName = BuildConfig.VERSION_NAME
    val appVersion = "Version: $versionName"
    val modelName = "Model: ${Build.MODEL}"
    val androidVersion = "Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val openIconShapeDialog = remember { mutableStateOf(false) }
    val openThemeDialog = remember { mutableStateOf(false) }

    ScaffoldWithLargeTopAppBar(
        title = redomi
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
                val listName = stringResource(id = R.string.list_format, stringResource(ListType.entries[listType].toRes))
                SettingsItem(
                    title = stringResource(id = R.string.layout),
                    icon = R.drawable.ic_format_list_bulleted,
                    description = listName.lowercase().replaceFirstChar { it.uppercase() }
                ) {
                    navController.navigate(layoutPage)
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.icon_shape),
                    icon = R.drawable.ic_category_filled,
                    description = stringResource(id = IconShape.entries[iconShape].toRes)
                ) {
                    openIconShapeDialog.value = true
                }
            }

            item {
                SettingsItem(
                    title = stringResource(id = R.string.theme),
                    icon = R.drawable.ic_color_lens_filled,
                    description = stringResource(id = Theme.entries[themeMode].toRes)
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
                val repository = stringResource(id = R.string.repository)
                SettingsItem(
                    title = stringResource(id = R.string.github),
                    icon = R.drawable.ic_github,
                    description = stringResource(id = R.string.github_description, repository)
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

    if (isFirstTime!! && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        DefaultDialog(
            icon =  R.drawable.ic_description,
            title = stringResource(id = R.string.dialog_setup_title),
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.dialog_setup_description, redomi))
                    IconItemDialog(
                        icon = R.drawable.ic_done_all,
                        description = stringResource(id = R.string.dialog_setup_description_checked, redomi)
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

    if (openIconShapeDialog.value) {
        DefaultDialog(
            icon =  R.drawable.ic_category_outline,
            title = stringResource(id = R.string.icon_shape),
            content = {
                IconShape.entries.forEach { item ->
                    RadioButtonItem(
                        item = item,
                        isSelected = item == IconShape.entries[iconShape],
                        horizontalPadding = 0.dp,
                        startPadding = 15.dp,
                        onClick = dataStoreViewModel::setIconShape
                    )
                }
            },
            onDismissRequest = { openIconShapeDialog.value = false },
            onConfirmAction = { openIconShapeDialog.value = false }
        )
    }

    if (openThemeDialog.value) {
        DefaultDialog(
            icon =  R.drawable.ic_color_lens_outline,
            title = stringResource(id = R.string.theme),
            content = {
                Theme.entries.forEach { item ->
                    RadioButtonItem(
                        item = item,
                        isSelected = item == Theme.entries[themeMode],
                        horizontalPadding = 0.dp,
                        startPadding = 15.dp,
                        onClick = dataStoreViewModel::setThemeMode
                    )
                }
            },
            onDismissRequest = { openThemeDialog.value = false },
            onConfirmAction = { openThemeDialog.value = false }
        )
    }
}