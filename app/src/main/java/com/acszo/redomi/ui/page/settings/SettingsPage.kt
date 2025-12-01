package com.acszo.redomi.ui.page.settings

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.acszo.redomi.R
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.data.Theme
import com.acszo.redomi.isGithubBuild
import com.acszo.redomi.ui.common.DefaultDialog
import com.acszo.redomi.ui.common.IconDescription
import com.acszo.redomi.ui.common.RadioButtonItemDialog
import com.acszo.redomi.ui.common.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.ui.nav.Pages.APPS_PAGE
import com.acszo.redomi.ui.nav.Pages.LAYOUT_PAGE
import com.acszo.redomi.ui.nav.Pages.UPDATE_PAGE
import com.acszo.redomi.ui.theme.bottomItemShape
import com.acszo.redomi.ui.theme.middleSmallItemShape
import com.acszo.redomi.ui.theme.middleLargeItemShape
import com.acszo.redomi.ui.theme.topItemShape
import com.acszo.redomi.utils.ClipboardUtils.copyText
import com.acszo.redomi.utils.IntentUtil.onIntentOpenDefaultsApp
import com.acszo.redomi.versionName
import com.acszo.redomi.viewmodel.DataStoreViewModel
import java.util.Locale
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
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
    val countryCode by dataStoreViewModel.countryCode.collectAsStateWithLifecycle()

    val redomi = stringResource(id = R.string.app_name)

    val uriHandle = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current
    val appVersion = "Version: $versionName"
    val modelName = "Model: ${Build.MODEL}"
    val androidVersion = "Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    var openIconShapeDialog by remember { mutableStateOf(false) }
    var openThemeDialog by remember { mutableStateOf(false) }
    var openCountryCodeDialog by remember { mutableStateOf(false) }

    ScaffoldWithLargeTopAppBar(
        title = redomi
    ) { padding, pageTitle ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                pageTitle()
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_grid_view,
                    title = stringResource(id = R.string.apps),
                    description = stringResource(id = R.string.apps_description),
                    itemShape = topItemShape
                ) {
                    navController.navigate(APPS_PAGE)
                }
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_format_list_bulleted,
                    title = stringResource(id = R.string.layout),
                    description = stringResource(id = ListOrientation.entries[listOrientation].toRes),
                    itemShape = middleSmallItemShape
                ) {
                    navController.navigate(LAYOUT_PAGE)
                }
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_category_filled,
                    title = stringResource(id = R.string.icon_shape),
                    description = stringResource(id = IconShape.entries[iconShape].toRes),
                    itemShape = middleSmallItemShape
                ) {
                    openIconShapeDialog = true
                }
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_color_lens_filled,
                    title = stringResource(id = R.string.theme),
                    description = stringResource(id = Theme.entries[themeMode].toRes),
                    itemShape = bottomItemShape
                ) {
                    openThemeDialog = true
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_world,
                    title = stringResource(id = R.string.country_code),
                    description = countryCode,
                    itemShape = middleLargeItemShape
                ) {
                    openCountryCodeDialog = true
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isGithubBuild) {
                item {
                    SettingsItem(
                        icon = R.drawable.ic_update,
                        title = stringResource(id = R.string.update),
                        description = stringResource(id = R.string.update_description),
                        itemShape = topItemShape,
                        trailingItem = {
                            if (isUpdateAvailable) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_new_releases_outline),
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.error,
                                    contentDescription = stringResource(id = R.string.update),
                                )
                            }
                        }
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
                    description = stringResource(id = R.string.github_description, repository),
                    itemShape = if (isGithubBuild) middleSmallItemShape else topItemShape
                ) {
                    uriHandle.openUri("https://github.com/acszo/Redomi")
                }
            }

            item {
                SettingsItem(
                    icon = R.drawable.ic_info_filled,
                    title = stringResource(id = R.string.version),
                    description = versionName,
                    itemShape = bottomItemShape
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

    if (openIconShapeDialog) {
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
            onDismissRequest = { openIconShapeDialog = false },
            onConfirmAction = { openIconShapeDialog = false }
        )
    }

    if (openThemeDialog) {
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
            onDismissRequest = { openThemeDialog = false },
            onConfirmAction = { openThemeDialog = false }
        )
    }

    if (openCountryCodeDialog) {
        var selectedItem by remember { mutableStateOf<String?>(null) }
        val countries = Locale.getISOCountries().associateWith { code ->
            val name = Locale.Builder()
                .setRegion(code)
                .build()
                .getDisplayCountry(getDefault())

            "$name ($code)"
        }
        val textFieldState = rememberTextFieldState()
        val filteredCountries = countries.filter {
            it.value.lowercase().contains(
            textFieldState.text.toString().lowercase())
        }
        val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
        val expanded = allowExpanded && countries.isNotEmpty()

        DefaultDialog(
            icon = R.drawable.ic_world,
            title = stringResource(id = R.string.country_code),
            content = {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = setExpanded
                ) {
                    TextField(
                        modifier = Modifier.width(280.dp).menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                        state = textFieldState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded,
                                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable),
                            )
                        },
                    )
                    ExposedDropdownMenu(
                        modifier = Modifier.heightIn(max = 280.dp),
                        expanded = expanded,
                        onDismissRequest = { setExpanded(false) },
                    ) {
                        filteredCountries.forEach { (code, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    textFieldState.setTextAndPlaceCursorAtEnd(name)
                                    selectedItem = code
                                    setExpanded(false)
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            },
            onDismissRequest = { openCountryCodeDialog = false },
            enabledConfirmAction = selectedItem != null,
            onConfirmAction = {
                dataStoreViewModel.setCountryCode(selectedItem!!)
                openCountryCodeDialog = false
            }
        )
    }

}