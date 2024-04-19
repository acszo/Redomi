package com.acszo.redomi.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R

@Composable
fun SettingsItem(
    title: String,
    icon: Int,
    description: String,
    isAlertIconVisible: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .clickable { onClick() }
            .requiredWidth(LocalConfiguration.current.screenWidthDp.dp)
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = title,
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = description,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isAlertIconVisible) {
            Icon(
                painter = painterResource(id = R.drawable.ic_new_releases_outline),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = stringResource(id = R.string.update),
            )
        }
    }
}