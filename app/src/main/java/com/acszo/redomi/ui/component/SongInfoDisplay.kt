package com.acszo.redomi.ui.component

import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.acszo.redomi.MainActivity
import com.acszo.redomi.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongInfoDisplay(
    thumbnail: String,
    title: String,
    artist: String,
    isUpdateAvailable: Boolean
) {
    val context = LocalContext.current
    val image = rememberAsyncImagePainter(model = thumbnail)

    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
    val errorColor = MaterialTheme.colorScheme.error

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
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (image.state is AsyncImagePainter.State.Loading) {
                Icon(
                    painter = painterResource(id = R.drawable.song_fill_icon),
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = stringResource(id = R.string.placeholder)
                )
            }
            Image(
                modifier = Modifier.size(80.dp),
                painter = image,
                contentScale = ContentScale.FillHeight,
                contentDescription = stringResource(id = R.string.song_cover),
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
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        ) {
            val currentRotation by remember { mutableFloatStateOf(120f) }
            val rotation = remember { Animatable(currentRotation) }

            LaunchedEffect(currentRotation) {
                rotation.animateTo(
                    targetValue = 180f,
                    animationSpec = tween(300, easing = LinearOutSlowInEasing),
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.settings_icon),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer { rotationZ = rotation.value },
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = stringResource(id = R.string.settings)
            )

            if (isUpdateAvailable) {
                Canvas(
                    modifier = Modifier.size(14.dp).offset(9.dp, -(6.dp))
                ) {
                    drawCircle(color = surfaceColor)
                }
                Canvas(
                    modifier = Modifier.size(8.dp).offset(9.dp, -(6.dp))
                ) {
                    drawCircle(color = errorColor)
                }
            }
        }
    }
}
