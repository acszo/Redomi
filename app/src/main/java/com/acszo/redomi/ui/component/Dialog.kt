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
import com.acszo.redomi.R

@Composable
fun RedomiAlertDialog(
    icon: Int,
    title: String,
    content: @Composable () -> Unit,
    confirmAction: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
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
        confirmButton = {
            DialogTextButton(
                onClick = { confirmAction() },
                text = stringResource(id = R.string.dialog_confirm)
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