package com.acszo.redomi.ui.page

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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.R
import com.acszo.redomi.data.DataStoreConst.BIG_GRID
import com.acszo.redomi.data.DataStoreConst.SMALL_GRID
import com.acszo.redomi.data.DataStoreConst.VERTICAL_LIST
import com.acszo.redomi.data.DataStoreConst.listTypes
import com.acszo.redomi.ui.component.AnimatedRadiusButton
import com.acszo.redomi.ui.component.RadioButtonItem
import com.acszo.redomi.ui.common.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.viewmodel.DataStoreViewModel
import kotlinx.coroutines.launch

@Composable
fun LayoutPage(
    dataStoreViewModel: DataStoreViewModel,
    backButton: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listType by dataStoreViewModel.layoutListType.collectAsStateWithLifecycle()
    val gridSize by dataStoreViewModel.layoutGridSize.collectAsStateWithLifecycle()

    ScaffoldWithLargeTopAppBar(
        title = stringResource(id = R.string.layout),
        description = stringResource(id = R.string.layout_description_page),
        backButton = { backButton() }
    ) { padding, pageTitleWithDescription ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            item {
                pageTitleWithDescription()
            }

            item {
                Text(
                    text = stringResource(id = R.string.list),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            item {
                listTypes.forEach { item ->
                    RadioButtonItem(
                        modifier = Modifier.requiredWidth(LocalConfiguration.current.screenWidthDp.dp),
                        value = listTypes[listType]!!,
                        text = item.value,
                        verticalPadding = 24.dp,
                        fontSize = 20.sp
                    ) {
                        scope.launch {
                            dataStoreViewModel.setLayoutListType(item.key)
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = listType == VERTICAL_LIST,
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
                            text = stringResource(id = R.string.layout_grid_size),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (grid in SMALL_GRID..BIG_GRID) {
                                AnimatedRadiusButton(
                                    isSelected = gridSize == grid,
                                    size = 80.dp,
                                    backgroundColor = if (gridSize == grid) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    text = grid.toString(),
                                    textColor = if (gridSize == grid) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                ) {
                                    scope.launch {
                                        dataStoreViewModel.setLayoutGridSize(grid)
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