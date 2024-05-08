package com.acszo.redomi.ui.bottom_sheet

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R
import com.acszo.redomi.service.Api.URL_YOUTUBE_QUERY
import com.acszo.redomi.utils.IntentUtil.onIntentView
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun ResultNotFound(
    querySong: String?
) {
    val context = LocalContext.current
    val animatedIcon = AnimatedImageVector.animatedVectorResource(R.drawable.anim_ic_not_found)
    var atEnd by remember { mutableStateOf(false) }

    // who thought this was a good idea??????
    LaunchedEffect(Unit) {
        delay(500)
        atEnd = true
    }

    Column(
        modifier = Modifier.size(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Icon(
            painter = rememberAnimatedVectorPainter(animatedIcon, atEnd),
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(80.dp),
            contentDescription = stringResource(id = R.string.no_result_found),
        )

        Text(
            stringResource(id = R.string.no_result_found),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium,
        )

        if (querySong != null) {
            FilledTonalButton(
                onClick = { onIntentView(context, URL_YOUTUBE_QUERY + querySong) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(id = R.string.search_on, stringResource(id = R.string.youtube)),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}