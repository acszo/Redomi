package com.acszo.redomi.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acszo.redomi.R
import com.acszo.redomi.data.Resource

@Composable
fun BackButton(
    backAction: () -> Unit
) {
    IconButton(
        onClick = { backAction() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = stringResource(id = R.string.back)
        )
    }
}

@Composable
fun <T> RadioButtonItemDialog(
    item: T,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) where T : Enum<T>, T : Resource = RadioButtonItemPage(
    modifier = Modifier.clip(MaterialTheme.shapes.small),
    item = item,
    isSelected = isSelected,
    horizontalPadding = 0.dp,
    startPadding = 15.dp,
    onClick = onClick
)


@Composable
fun <T> RadioButtonItemPage(
    item: T,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) where T : Enum<T>, T : Resource = RadioButtonItemPage(
    modifier = Modifier.ignoreHorizontalPadding(),
    item = item,
    isSelected = isSelected,
    verticalPadding = 24.dp,
    fontSize = 20.sp,
    onClick = onClick
)

@Composable
private fun <T> RadioButtonItemPage(
    modifier: Modifier = Modifier,
    item: T,
    isSelected: Boolean,
    verticalPadding: Dp = 10.dp,
    horizontalPadding: Dp = 18.dp,
    startPadding: Dp = 0.dp,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    onClick: (Int) -> Unit
) where T : Enum<T>, T : Resource {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onClick(item.ordinal) }
            )
            .padding(vertical = verticalPadding, horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            modifier = Modifier.padding(start = startPadding),
            selected = isSelected,
            onClick = null,
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = stringResource(id = item.toRes),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = fontSize,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            ),
        )
    }
}

@Composable
fun AnimatedRadiusButton(
    isSelected: Boolean,
    size: Dp,
    text: String,
    onClick: () -> Unit
) {
    val radius by animateDpAsState(
        targetValue = if (isSelected) 22.dp else size / 2,
        label = ""
    )

    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                shape = RoundedCornerShape(radius)
                clip = true
            }
            .background(color = selectedBoxColor(isSelected))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = selectedTextColor(isSelected),
            style = MaterialTheme.typography.headlineLarge.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                fontWeight = FontWeight.W500,
            )
        )
    }
}

@Composable
fun selectedBoxColor(isSelected: Boolean) =
    if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceVariant

@Composable
fun selectedTextColor(isSelected: Boolean) =
    if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurfaceVariant