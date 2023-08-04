package com.acszo.redomi.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.component.AnimatedRadiusButton
import com.acszo.redomi.ui.component.PageTitle
import com.acszo.redomi.ui.component.RadioButtonItem
import com.acszo.redomi.ui.component.SmallTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutPage(
    backButton: @Composable () -> Unit
) {
    val pageTitle: String = stringResource(id = R.string.layout)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val listOptions: List<String> = listOf(
        stringResource(id = R.string.horizontal_list),
        stringResource(id = R.string.vertical_list)
    )

    val dataStore = SettingsDataStore(context)
    val listType = dataStore.getLayoutListType.collectAsState(initial = "")
    val gridSize = dataStore.getLayoutGridSize.collectAsState(initial = 0)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = pageTitle,
                scrollBehavior = scrollBehavior,
                navigationIcon = { backButton() }
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            Column {
                PageTitle(
                    title = pageTitle,
                    scrollBehavior = scrollBehavior
                )
                Text(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    text = stringResource(id = R.string.layout_description_page),
                )
            }

            listOptions.forEach { text ->
                RadioButtonItem(
                    value = listType.value,
                    text = text,
                ) {
                    scope.launch {
                        dataStore.saveLayoutListType(text)
                    }
                }
            }

            if (listType.value == stringResource(id = R.string.vertical_list)) {
                Text(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    text = stringResource(id = R.string.layout_grid_size),
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 2..4) {
                        AnimatedRadiusButton(
                            isSelected = gridSize.value == i,
                            size = 80.dp,
                            backgroundColor = if (gridSize.value == i) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            text = i.toString(),
                            textColor = if (gridSize.value == i) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ) {
                            scope.launch {
                                dataStore.saveLayoutGridSize(i)
                            }
                        }
                    }
                }
            }
        }
    }
}