package com.acszo.redomi.ui.component

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.acszo.redomi.R

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
fun RadioButtonItem(
    modifier: Modifier = Modifier,
    value: Int,
    isSelected: Boolean,
    verticalPadding: Dp = 10.dp,
    horizontalPadding: Dp = 28.dp,
    startPadding: Dp = 0.dp,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick
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
            text = stringResource(id = value),
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
    backgroundColor: Color,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    val radius = animateDpAsState(
        targetValue = if (isSelected) 22.dp else size / 2,
        label = ""
    )
    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                shape = RoundedCornerShape(radius.value)
                clip = true
            }
            .background(color = backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
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
fun DialogTextButton(
    onClick: () -> Unit,
    text: String
) {
    TextButton(
        onClick = { onClick() }
    ) {
        Text(text = text)
    }
}