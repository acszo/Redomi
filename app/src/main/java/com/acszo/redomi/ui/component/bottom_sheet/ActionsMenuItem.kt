package com.acszo.redomi.ui.component.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.ui.component.ClickableItem

@Composable
fun ActionsMenuItem(
    label: Int,
    icon: Int,
    onClickAction: () -> Unit
) {
    ClickableItem(
        @Composable {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClickAction()
                    }
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .size(70.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = stringResource(label)
                )
            }
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}