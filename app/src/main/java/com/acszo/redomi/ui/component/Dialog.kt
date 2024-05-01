package com.acszo.redomi.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DefaultDialog(
    icon: Int,
    title: String,
    content: @Composable () -> Unit,
    onDismissRequest: () -> Unit = {},
    onConfirmAction: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = icon),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = title
            )
        },
        title = { Text(text = title) },
        text = {
            Column {
                content()
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            DialogTextButton(
                onClick = onConfirmAction,
                text = stringResource(id = android.R.string.ok)
            )
        }
    )
}

@Composable
fun IconItemDialog(
    icon: Int,
    description: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Text(text = description)
    }
}