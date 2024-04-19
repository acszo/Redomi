package com.acszo.redomi.ui.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import com.acszo.redomi.data.IconShape

@Composable
fun AppCheckBoxItem(
    icon: Int,
    iconShape: Int,
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
            contentDescription = title,
            tint = MaterialTheme.colorScheme.secondary,
        )
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(IconShape.valueOf(iconShape)!!.radius),
            painter = painterResource(id = icon),
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