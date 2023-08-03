package com.acszo.redomi.ui.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import com.acszo.redomi.data.SettingsDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutPage(
    backButton: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listOptions: List<String> = listOf(
        stringResource(id = R.string.horizontal_list),
        stringResource(id = R.string.vertical_list)
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = SettingsDataStore(context)
    val listType = dataStore.getLayoutListType.collectAsState(initial = "")
    val gridSize = dataStore.getLayoutGridSize.collectAsState(initial = 0)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.layout)
                    )
                },
                navigationIcon = { backButton() },
                scrollBehavior = scrollBehavior,
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 28.dp),
                text = stringResource(id = R.string.layout_description_page),

            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOptions.forEach { text ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(
                                color = if (listType.value == text) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                            )
                            .clickable {
                                scope.launch {
                                    dataStore.saveLayoutListType(text)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 15.dp),
                            text = text,
                            color = if (listType.value == text) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.W500,
                            )
                        )
                    }
                }
            }

            if (listType.value == stringResource(id = R.string.vertical_list)) {
                val size = 80.dp
                Text(
                    modifier = Modifier
                        .padding(horizontal = 28.dp),
                    text = stringResource(id = R.string.layout_grid_size),
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 2..4) {
                        val radius = animateDpAsState(targetValue = if (gridSize.value == i) 22.dp else size / 2,
                            label = ""
                        )
                        Box(
                            modifier = Modifier
                                .size(size)
                                .clip(RoundedCornerShape(radius.value))
                                .background(
                                    color = if (gridSize.value == i) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                )
                                .clickable {
                                    scope.launch {
                                        dataStore.saveLayoutGridSize(i)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = i.toString(),
                                color = if (gridSize.value == i) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.W500,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}