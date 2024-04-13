package com.acszo.redomi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.acszo.redomi.MainActivity
import com.acszo.redomi.data.Theme

private val darkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    tertiary = TertiaryLight
)

private val lightColorScheme = lightColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark
)

@Composable
fun RedomiTheme(
    currentTheme: Int,
    content: @Composable () -> Unit
) {
    val getTheme = Theme.valueOf(currentTheme)!!.mode()

    val colorScheme = when {
         Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (getTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        getTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as Activity
            val window = (activity).window
            when (activity) {
                is MainActivity -> {
                    window.decorView.setBackgroundColor(colorScheme.surface.toArgb())
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !getTheme
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}