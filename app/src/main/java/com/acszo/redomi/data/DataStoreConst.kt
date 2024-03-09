package com.acszo.redomi.data

import com.acszo.redomi.R

object DataStoreConst {

    const val FIRST_TIME = "first_time"
    const val LAYOUT_LIST_TYPE = "list_type"
    const val LAYOUT_GRID_SIZE = "grid_size"
    const val THEME_MODE = "theme_mode"

    const val HORIZONTAL_LIST = 0
    const val VERTICAL_LIST = 1

    const val SMALL_GRID = 2
    const val MEDIUM_GRID = 3
    const val BIG_GRID = 4

    const val SYSTEM_THEME = 0
    const val DARK_THEME = 1
    const val LIGHT_THEME = 2

    val listTypes = mapOf(
        HORIZONTAL_LIST to R.string.horizontal_list,
        VERTICAL_LIST to R.string.vertical_list
    )

    val themes = mapOf(
        SYSTEM_THEME to R.string.system_theme,
        DARK_THEME to R.string.dark_theme,
        LIGHT_THEME to R.string.light_theme
    )

}