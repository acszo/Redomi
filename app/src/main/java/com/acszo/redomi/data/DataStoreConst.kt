package com.acszo.redomi.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import com.acszo.redomi.R
import sv.lib.squircleshape.SquircleShape

object DataStoreConst {

    const val FIRST_TIME = "first_time"
    const val LAYOUT_LIST_TYPE = "list_type"
    const val LAYOUT_GRID_SIZE = "grid_size"
    const val ICON_SHAPE = "icon_shape"
    const val THEME_MODE = "theme_mode"

    const val HORIZONTAL_LIST = 0
    const val VERTICAL_LIST = 1

    const val SMALL_GRID = 2
    const val MEDIUM_GRID = 3
    const val BIG_GRID = 4

    val listTypes = mapOf(
        HORIZONTAL_LIST to R.string.horizontal_list,
        VERTICAL_LIST to R.string.vertical_list
    )

}

enum class IconShape(val value: Int) {
    SQUIRCLE(0),
    CIRCLE(1),
    SQUARE(2);

    val toString
        get() = when (this) {
            SQUIRCLE -> R.string.squircle_icon
            CIRCLE -> R.string.circle_icon
            SQUARE -> R.string.square_icon
        }

    val radius
        get() = when (this) {
            SQUIRCLE -> SquircleShape()              // 👍👍👍
            CIRCLE -> CircleShape                    // 👍👍👍
            SQUARE -> RoundedCornerShape(25) // 👎
        }

    companion object {
        fun valueOf(value: Int) = entries.find { it.value == value }
    }
}

enum class Theme(val value: Int) {
    SYSTEM_THEME(0),
    DARK_THEME(1),
    LIGHT_THEME(2);

    val toString
        get() = when (this) {
            SYSTEM_THEME -> R.string.system_theme
            DARK_THEME -> R.string.dark_theme
            LIGHT_THEME -> R.string.light_theme
        }

    @Composable
    fun mode() = when (this) {
        SYSTEM_THEME -> isSystemInDarkTheme()
        DARK_THEME -> true
        LIGHT_THEME -> false
    }

    companion object {
        fun valueOf(value: Int) = entries.find { it.value == value }
    }
}