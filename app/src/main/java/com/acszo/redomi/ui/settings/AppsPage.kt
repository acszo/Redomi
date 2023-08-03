package com.acszo.redomi.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import com.acszo.redomi.ui.component.PageTitle
import com.acszo.redomi.ui.component.SmallTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsPage(
    backButton: @Composable () -> Unit
) {
    val pageTitle: String = stringResource(id = R.string.apps)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            PageTitle(title = pageTitle)
        }
    }
}