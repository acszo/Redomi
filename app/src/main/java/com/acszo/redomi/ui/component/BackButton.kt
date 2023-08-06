package com.acszo.redomi.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.acszo.redomi.R

@Composable
fun BackButton(
    backAction: () -> Unit
) {
    IconButton(
        onClick = { backAction() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back_arrow_icon),
            contentDescription = stringResource(id = R.string.back)
        )
    }
}