package com.acszo.redomi.ui.page.settings

import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.acszo.redomi.R
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.data.Theme
import com.acszo.redomi.isGithubBuild
import com.acszo.redomi.ui.common.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.ui.component.DefaultDialog
import com.acszo.redomi.ui.component.IconDescription
import com.acszo.redomi.ui.component.RadioButtonItemDialog
import com.acszo.redomi.ui.nav.Pages.APPS_PAGE
import com.acszo.redomi.ui.nav.Pages.LAYOUT_PAGE
import com.acszo.redomi.ui.nav.Pages.UPDATE_PAGE
import com.acszo.redomi.utils.ClipboardUtils.copyText
import com.acszo.redomi.utils.IntentUtil.onIntentOpenDefaultsApp
import com.acszo.redomi.versionName
import com.acszo.redomi.viewmodel.DataStoreViewModel

@Composable
fun SettingsPage(
    dataStoreViewModel: DataStoreViewModel,
    navController: NavController,
    isUpdateAvailable: Boolean
) {
    val context = LocalContext.current

    val isFirstTime by dataStoreViewModel.isFirstTime.collectAsStateWithLifecycle()
    val listOrientation by dataStoreViewModel.listOrientation.collectAsStateWithLifecycle()
    val iconShape by dataStoreViewModel.iconShape.collectAsStateWithLifecycle()
    val themeMode by dataStoreViewModel.themeMode.collectAsStateWithLifecycle()

    val redomi = stringResource(id = R.string.app_name)

    val uriHandle = LocalUriHandler.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val appVersion = "Version: $versionName"
    val modelName = "Model: ${Build.MODEL}"
    val androidVersion = "Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    val openIconShapeDialog = remember { mutableStateOf(false) }
    val openThemeDialog = remember { mutableStateOf(false) }

    ScaffoldWithLargeTopAppBar(
        title = redomi
    ) { padding, pageTitle ->
        LazyColumn(
            contentPadding = padding,
        ) {
            item {
                pageTitle()
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_grid_view,
                    title = stringResource(id = R.string.apps),
                    description = stringResource(id = R.string.apps_description)
                ) {
                    navController.navigate(APPS_PAGE)
                }
            }

            item {
                val orientationText = stringResource(ListOrientation.entries[listOrientation].toRes)
                val listOrientationText = stringResource(id = R.string.list_format, orientationText)
                SettingsItem(
                    icon = R.drawable.ic_format_list_bulleted,
                    title = stringResource(id = R.string.layout),
                    description = listOrientationText.lowercase().replaceFirstChar { it.uppercase() }
                ) {
                    navController.navigate(LAYOUT_PAGE)
                }
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_category_filled,
                    title = stringResource(id = R.string.icon_shape),
                    description = stringResource(id = IconShape.entries[iconShape].toRes)
                ) {
                    openIconShapeDialog.value = true
                }
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_color_lens_filled,
                    title = stringResource(id = R.string.theme),
                    description = stringResource(id = Theme.entries[themeMode].toRes)
                ) {
                    openThemeDialog.value = true
                }
            }

            if (isGithubBuild) {
                item {
                    SettingsItem(
                        icon = R.drawable.ic_update,
                        title = stringResource(id = R.string.update),
                        description = stringResource(id = R.string.update_description),
                        isAlertIconVisible = isUpdateAvailable
                    ) {
                        navController.navigate(UPDATE_PAGE)
                    }
                }
            }

            item {
                val repository = stringResource(id = R.string.repository)
                SettingsItem(
                    icon = R.drawable.ic_github,
                    title = stringResource(id = R.string.github),
                    description = stringResource(id = R.string.github_description, repository)
                ) {
                    uriHandle.openUri("https://github.com/acszo/Redomi")
                }
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_info_filled,
                    title = stringResource(id = R.string.version),
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

    if (isFirstTime && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        DefaultDialog(
            icon = R.drawable.ic_description,
            title = stringResource(id = R.string.dialog_setup_title),
            verticalSpaceBy = 16.dp,
            content = {
                Text(text = stringResource(id = R.string.dialog_setup_description, redomi))

                IconDescription(
                    icon = R.drawable.ic_done_all,
                    description = stringResource(id = R.string.dialog_setup_description_checked, redomi)
                )

                IconDescription(
                    icon = R.drawable.ic_remove_done,
                    description = stringResource(id = R.string.dialog_setup_description_unchecked)
                )
            },
            onConfirmAction = {
                dataStoreViewModel.setIsFirstTime()
                onIntentOpenDefaultsApp(context)
            }
        )
    }

    if (openIconShapeDialog.value) {
        DefaultDialog(
            icon = R.drawable.ic_category_outline,
            title = stringResource(id = R.string.icon_shape),
            content = {
                IconShape.entries.forEach { item ->
                    RadioButtonItemDialog(
                        item = item,
                        isSelected = item == IconShape.entries[iconShape],
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
            icon = R.drawable.ic_color_lens_outline,
            title = stringResource(id = R.string.theme),
            content = {
                Theme.entries.forEach { item ->
                    RadioButtonItemDialog(
                        item = item,
                        isSelected = item == Theme.entries[themeMode],
                        onClick = dataStoreViewModel::setThemeMode
                    )
                }
            },
            onDismissRequest = { openThemeDialog.value = false },
            onConfirmAction = { openThemeDialog.value = false }
        )
    }
}