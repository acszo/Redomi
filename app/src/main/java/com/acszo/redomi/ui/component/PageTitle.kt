package com.acszo.redomi.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PageTitle(title: String) {
    Text(
        modifier = Modifier
            .padding(top = 100.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        text = title,
        style = MaterialTheme.typography.displaySmall,
    )
}