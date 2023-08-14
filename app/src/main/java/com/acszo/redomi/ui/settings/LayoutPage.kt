package com.acszo.redomi.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import com.acszo.redomi.R
import com.acszo.redomi.data.DataStoreConst.BIG_GRID
import com.acszo.redomi.data.DataStoreConst.HORIZONTAL_LIST
import com.acszo.redomi.data.DataStoreConst.MEDIUM_GRID
import com.acszo.redomi.data.DataStoreConst.SMALL_GRID
import com.acszo.redomi.data.DataStoreConst.VERTICAL_LIST
import com.acszo.redomi.data.DataStoreConst.getListType
import com.acszo.redomi.data.DataStoreConst.listTypes
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.ui.component.AnimatedRadiusButton
import com.acszo.redomi.ui.component.PageDescription
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

    val dataStore = SettingsDataStore(context)
    val listType = dataStore.getLayoutListType.collectAsState(initial = HORIZONTAL_LIST)
    val gridSize = dataStore.getLayoutGridSize.collectAsState(initial = MEDIUM_GRID)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = pageTitle,
                scrollBehavior = scrollBehavior,
                navigationIcon = { backButton() }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            contentPadding = WindowInsets.systemBars.asPaddingValues()
        ) {
            item {
                Column {
                    PageTitle(
                        title = pageTitle,
                        scrollBehavior = scrollBehavior
                    )
                    PageDescription(description = stringResource(id = R.string.layout_description_page))
                }
            }

            listTypes.forEach { item ->
                item {
                    RadioButtonItem(
                        value = getListType(listType.value!!),
                        text = item.value,
                        fontSize = 20.sp
                    ) {
                        scope.launch {
                            dataStore.saveLayoutListType(item.key)
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = listType.value == VERTICAL_LIST,
                    enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically(targetOffsetY = { -40 }) + fadeOut(
                        animationSpec = tween(
                            200
                        )
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(bottom = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 28.dp),
                            text = stringResource(id = R.string.layout_grid_size),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (grid in SMALL_GRID..BIG_GRID) {
                                AnimatedRadiusButton(
                                    isSelected = gridSize.value == grid,
                                    size = 80.dp,
                                    backgroundColor = if (gridSize.value == grid) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    text = grid.toString(),
                                    textColor = if (gridSize.value == grid) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                ) {
                                    scope.launch {
                                        dataStore.saveLayoutGridSize(grid)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}