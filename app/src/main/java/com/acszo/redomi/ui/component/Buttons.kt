package com.acszo.redomi.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonItem(
    value: String?,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = (value == text),
                onClick = { onClick() }
            )
            .padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (value == text),
            onClick = { onClick() }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
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
            .clip(RoundedCornerShape(radius.value))
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