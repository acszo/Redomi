package com.acszo.redomi.ui.page.settings.apps

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R

@Composable
fun AppCheckBoxItem(
    @DrawableRes icon: Int,
    iconShape: Shape,
    title: String,
    size: Int,
    isChecked: Boolean,
    onCheckedAction: () -> Unit,
    onUnCheckedAction: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = title,
        )
        Image(
            painter = painterResource(id = icon),
            modifier = Modifier
                .size(40.dp)
                .clip(iconShape),
            contentDescription = title,
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    onCheckedAction()
                } else {
                    if (size > 1) onUnCheckedAction()
                }
            },
        )
    }
}