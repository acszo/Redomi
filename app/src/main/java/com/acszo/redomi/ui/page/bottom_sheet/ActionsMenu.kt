package com.acszo.redomi.ui.page.bottom_sheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import com.acszo.redomi.ui.component.ClickableItem
import com.acszo.redomi.utils.ClipboardUtils.copyText
import com.acszo.redomi.utils.IntentUtil.onIntentSend
import com.acszo.redomi.utils.IntentUtil.onIntentView

@Composable
fun ActionsMenu(
    url: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .height(150.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        ActionsItem(
            icon = R.drawable.ic_play,
            title = stringResource(id = R.string.open)
        ) {
            onIntentView(context, url)
            onDismiss()
        }

        ActionsItem(
            icon = R.drawable.ic_link,
            title = stringResource(id = android.R.string.copy)
        ) {
            copyText(clipboardManager, url)
            onDismiss()
        }

        ActionsItem(
            icon = R.drawable.ic_share,
            title = stringResource(id = R.string.share)
        ) {
            onIntentSend(context, url)
            onDismiss()
        }
    }
}

@Composable
private fun ActionsItem(
    @DrawableRes icon: Int,
    title: String,
    onClickAction: () -> Unit
) {
    ClickableItem {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClickAction() }
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
                contentDescription = title
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}