package com.acszo.redomi.data

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import com.acszo.redomi.R
import sv.lib.squircleshape.SquircleShape

object DataStoreConst {

    const val FIRST_TIME = "first_time"
    const val LIST_ORIENTATION = "list_type"
    const val GRID_SIZE = "grid_size"
    const val ICON_SHAPE = "icon_shape"
    const val THEME_MODE = "theme_mode"

    const val SMALL_GRID = 2
    const val MEDIUM_GRID = 3
    const val BIG_GRID = 4

}

interface Resource {
    @get:StringRes val toRes: Int
}

enum class ListOrientation: Resource {
    HORIZONTAL,
    VERTICAL;

    override val toRes: Int
        get() = when (this) {
            HORIZONTAL -> R.string.horizontal
            VERTICAL -> R.string.vertical
        }
}

enum class IconShape(val shape: Shape): Resource {
    SQUIRCLE(SquircleShape()),              // 👍👍👍
    CIRCLE(CircleShape),                    // 👍👍
    SQUARE(RoundedCornerShape(25)); // 👎

    override val toRes: Int
        get() = when (this) {
            SQUIRCLE -> R.string.squircle_icon
            CIRCLE -> R.string.circle_icon
            SQUARE -> R.string.square_icon
        }
}

enum class Theme: Resource {
    SYSTEM_THEME,
    DARK_THEME,
    LIGHT_THEME;

    @Composable
    fun mode(): Boolean
        = when (this) {
            SYSTEM_THEME -> isSystemInDarkTheme()
            DARK_THEME -> true
            LIGHT_THEME -> false
        }

    override val toRes: Int
        get() = when (this) {
            SYSTEM_THEME -> R.string.system_theme
            DARK_THEME -> R.string.dark_theme
            LIGHT_THEME -> R.string.light_theme
        }
}