package com.acszo.redomi.ui.page.settings.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R

@Composable
fun BottomInfo(
    text: String
) {
    Column(
        modifier = Modifier.padding(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_info_outline),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = stringResource(id = R.string.info),
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}