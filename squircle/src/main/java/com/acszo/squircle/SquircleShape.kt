package com.acszo.squircle

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object SquircleShape: Shape {

    private val CORNER_SIZE = CornerSize(100)
    private const val CORNER_SMOOTHING = 0.72f

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        var corner = CORNER_SIZE.toPx(size, density)
        val minDimension = size.minDimension

        if (corner * 2 > minDimension) {
            corner = minDimension / 2
        }

        return Outline.Generic(
            path = Path().apply {
                val width = size.width
                val height = size.height
                val smoothingFactor = corner * (1 - CORNER_SMOOTHING)

                moveTo(x = corner, y = 0f)

                lineTo(x = width - corner, y = 0f)
                cubicTo(x1 = width - smoothingFactor, y1 = 0f, x2 = width, y2 = smoothingFactor, x3 = width, y3 = corner)

                lineTo(x = width, y = height - corner)
                cubicTo(x1 = width, y1 = height - smoothingFactor, x2 = width - smoothingFactor, y2 = height, x3 = width - corner, y3 = height)

                lineTo(x = corner, y = height)
                cubicTo(x1 = smoothingFactor, y1 = height, x2 = 0f, y2 = height - smoothingFactor, x3 = 0f, y3 = height - corner)

                lineTo(x = 0f, y = corner)
                cubicTo(x1 = 0f, y1 = smoothingFactor, x2 = smoothingFactor, y2 = 0f, x3 = corner, y3 = 0f)

                close()
            }
        )
    }

}