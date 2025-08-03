package com.acszo.redomi.ui.page.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItem(
    @DrawableRes icon: Int,
    title: String,
    description: String,
    trailingItem: @Composable () -> Unit = {},
    itemShape: RoundedCornerShape,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .clip(itemShape)
            .clickable { onClick() },
        leadingContent = {
            Icon(
                painter = painterResource(icon),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = title,
            )
        },
        headlineContent = {
            Text(text = title)
        },
        supportingContent = {
            Text(text = description)
        },
        trailingContent = {
            trailingItem()
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}