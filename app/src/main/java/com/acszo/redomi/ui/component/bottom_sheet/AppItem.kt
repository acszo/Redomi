package com.acszo.redomi.ui.component.bottom_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.ui.component.ClickableItem
import com.acszo.redomi.utils.IntentUtil.onIntentView
import com.acszo.redomi.utils.StringUtil.separateUppercase

@Composable
fun AppItem(
    appDetail: AppDetails,
    link: String,
    isActionsRequired: Boolean,
    bringActions: MutableState<Boolean>,
    selectedPlatformLink: MutableState<String>
) {
    val context = LocalContext.current
    val title: String = separateUppercase(appDetail.title)
    val titleWords: List<String> = title.split("\\s+".toRegex())

    ClickableItem(
        @Composable {
            Image(
                painterResource(id = appDetail.icon),
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                contentDescription = titleWords[0],
            )
            Text(text = titleWords[0].trim())
            Text(text = if (titleWords.size > 1) titleWords[1] else "")
        },
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable {
                if (!isActionsRequired) {
                    onIntentView(context, link)
                } else {
                    bringActions.value = true
                    selectedPlatformLink.value = link
                }
            }
    )
}