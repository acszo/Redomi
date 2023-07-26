package com.acszo.redomi.util

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

class Clipboard {

    fun copyText(clipboardManager: ClipboardManager, text: String, onDismiss: () -> Unit = { }) {
        clipboardManager.setText(AnnotatedString(text))
        onDismiss()
    }

}