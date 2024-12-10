package com.acszo.redomi.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acszo.redomi.R
import com.acszo.redomi.data.DataStoreConst.BIG_GRID
import com.acszo.redomi.data.DataStoreConst.SMALL_GRID
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.ui.common.ScaffoldWithLargeTopAppBar
import com.acszo.redomi.ui.common.enterVerticalTransition
import com.acszo.redomi.ui.common.exitVerticalTransition
import com.acszo.redomi.ui.component.AnimatedRadiusButton
import com.acszo.redomi.ui.component.RadioButtonItemPage
import com.acszo.redomi.viewmodel.DataStoreViewModel

@Composable
fun LayoutPage(
    dataStoreViewModel: DataStoreViewModel,
    backButton: @Composable () -> Unit
) {
    val listOrientation by dataStoreViewModel.listOrientation.collectAsStateWithLifecycle()
    val gridSize by dataStoreViewModel.gridSize.collectAsStateWithLifecycle()

    ScaffoldWithLargeTopAppBar(
        title = stringResource(id = R.string.layout),
        description = stringResource(id = R.string.layout_description_page),
        backButton = { backButton() }
    ) { padding, pageTitleWithDescription ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(28.dp),
            contentPadding = padding,
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
                ListOrientation.entries.forEach { item ->
                    RadioButtonItemPage(
                        item = item,
                        isSelected = item == ListOrientation.entries[listOrientation],
                        onClick = dataStoreViewModel::setListOrientation
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = listOrientation == ListOrientation.VERTICAL.ordinal,
                    enter = enterVerticalTransition(),
                    exit = exitVerticalTransition(),
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
                                    text = grid.toString()
                                ) {
                                    dataStoreViewModel.setGridSize(grid)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}