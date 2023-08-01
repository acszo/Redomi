package com.acszo.redomi.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 28.dp),
        ) {
            Text(text = "List type:")
            listOptions.forEach { text ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (listType.value == text),
                            onClick = {
                                scope.launch {
                                    dataStore.saveLayoutListType(text)
                                }
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (listType.value == text),
                        onClick = {
                            scope.launch {
                                dataStore.saveLayoutListType(text)
                            }
                        }
                    )
                    Text(text = text)
                }
            }
        }
    }
}