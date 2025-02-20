package com.acszo.redomi.ui.page.bottom_sheet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acszo.redomi.R
import com.acszo.redomi.ui.component.RotatingIcon
import com.acszo.redomi.utils.IntentUtil.onIntentSettingsPage

@Composable
fun SongInfoDisplay(
    type: String,
    thumbnail: String,
    title: String,
    artist: String,
    isUpdateAvailable: Boolean
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = if (type == "album") painterResource(id = R.drawable.ic_album)
                else painterResource(id = R.drawable.ic_music_note),
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = null
            )

            AsyncImage(
                model = thumbnail,
                contentScale = ContentScale.FillHeight,
                contentDescription = stringResource(id = R.string.song_cover)
            )
        }

        Column(
            modifier = Modifier
                .height(80.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = title,
                modifier = Modifier.basicMarquee(),
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                )
            )
            Text(
                text = artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                ),
            )
        }

        IconButton(
            onClick = {
                onIntentSettingsPage(context)
            }
        ) {
            RotatingIcon(
                icon = R.drawable.ic_settings,
                size = 24.dp,
                startValue = 120f,
                contentDescription = stringResource(id = R.string.settings)
            )

            if (isUpdateAvailable) {
                val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                val errorColor = MaterialTheme.colorScheme.error

                Canvas(
                    modifier = Modifier
                        .size(14.dp)
                        .offset(9.dp, -(6.dp))
                ) {
                    drawCircle(color = surfaceColor)
                }
                Canvas(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(9.dp, -(6.dp))
                ) {
                    drawCircle(color = errorColor)
                }
            }
        }
    }
}