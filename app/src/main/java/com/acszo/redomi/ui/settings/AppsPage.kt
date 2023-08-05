package com.acszo.redomi.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import com.acszo.redomi.model.Platform.platforms
import com.acszo.redomi.ui.component.PageBottomInfo
import com.acszo.redomi.ui.component.PageDescription
import com.acszo.redomi.ui.component.PageTitle
import com.acszo.redomi.ui.component.SmallTopAppBar
import com.acszo.redomi.ui.component.Tabs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsPage(
    backButton: @Composable () -> Unit
) {
    val pageTitle: String = stringResource(id = R.string.apps)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val tabs: List<String> = listOf(
        stringResource(id = R.string.installed),
        stringResource(id = R.string.all)
    )
    val selectedTab = remember { mutableStateOf(tabs[0]) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            SmallTopAppBar(
                title = pageTitle,
                scrollBehavior = scrollBehavior,
                navigationIcon = { backButton() }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            Column {
                PageTitle(
                    title = pageTitle,
                    scrollBehavior = scrollBehavior
                )
                PageDescription(description = stringResource(id = R.string.apps_description_page))
            }
            Tabs(
                tabs = tabs,
                selectedTab = selectedTab
            )
            LazyColumn {
                items(items = platforms) { app ->
                    val title: String = app.title.replace("(?<=[^A-Z])(?=[A-Z])".toRegex(), " ")
                        .replaceFirstChar { it.uppercase() }
                    Row(
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.menu_icon),
                            contentDescription = title,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                        Image(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(id = app.icon),
                            contentDescription = title,
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = title,
                        )
                        Checkbox(
                            checked = true,
                            onCheckedChange = {},
                        )
                    }
                }
            }
            PageBottomInfo(
                if (selectedTab.value == tabs[0])
                    stringResource(id = R.string.installed_tab_info)
                else
                    stringResource(id = R.string.all_tab_info)
            )
        }
    }
}