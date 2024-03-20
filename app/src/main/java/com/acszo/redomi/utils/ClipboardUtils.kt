package com.acszo.redomi.utils

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

object ClipboardUtils {

    fun copyText(clipboardManager: ClipboardManager, text: String) {
        clipboardManager.setText(AnnotatedString(text))
    }

}