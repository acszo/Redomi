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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R

@Composable
fun AppCheckBoxItem(
    icon: Int,
    title: String,
    isChecked: Boolean,
    onCheckedAction: () -> Unit,
    onUnCheckedAction: () -> Unit,
) {
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
            painter = painterResource(id = icon),
            contentDescription = title,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = title,
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    onCheckedAction()
                } else {
                    onUnCheckedAction()
                }
            },
        )
    }
}