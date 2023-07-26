package com.acszo.redomi.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.acszo.redomi.R
import com.acszo.redomi.BuildConfig
import com.acszo.redomi.ui.component.PageTitle
import com.acszo.redomi.ui.component.SettingsItem

@Composable
fun SettingsPage() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            PageTitle("Redomi")
        }
    ) {
        LazyColumn(
                Modifier.padding(it),
        ) {
            item {
                SettingsItem(
                    title = stringResource(id = R.string.apps),
                    icon = R.drawable.grid_view_icon,
                    description = stringResource(id = R.string.apps_description)
                ) {

                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.layout),
                    icon = R.drawable.format_list_bulleted_icon,
                    description = stringResource(id = R.string.layout_description)
                ) {

                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.theme),
                    icon = R.drawable.color_lens_icon,
                    description = stringResource(id = R.string.theme_description)
                ) {

                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.github),
                    icon = R.drawable.github_icon,
                    description = stringResource(id = R.string.github_description)
                ) {

                }
            }
            item {
                SettingsItem(
                    title = stringResource(id = R.string.version),
                    icon = R.drawable.info_icon,
                    description = BuildConfig.VERSION_NAME
                ) {

                }
            }
        }
    }
}