package com.acszo.redomi.ui.component.common_page

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
fun PageBottomInfo(
    text: String
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 28.dp)
            .padding(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.info_outline_icon),
            contentDescription = stringResource(id = R.string.info),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}