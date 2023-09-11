package com.acszo.redomi.utils

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

object ClipboardUtils {

    fun copyText(clipboardManager: ClipboardManager, text: String, onDismiss: () -> Unit = { }) {
        clipboardManager.setText(AnnotatedString(text))
        onDismiss()
    }

}