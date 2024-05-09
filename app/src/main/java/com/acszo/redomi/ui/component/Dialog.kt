package com.acszo.redomi.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DefaultDialog(
    @DrawableRes icon: Int,
    title: String,
    verticalSpaceBy: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit,
    onDismissRequest: () -> Unit = {},
    onConfirmAction: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = icon),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null
            )
        },
        title = { Text(text = title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(verticalSpaceBy)
            ) {
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
    @DrawableRes icon: Int,
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
        Text(
            text = description
        )
    }
}