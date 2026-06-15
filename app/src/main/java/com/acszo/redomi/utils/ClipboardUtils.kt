package com.acszo.redomi.utils

import android.content.ClipData
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.toClipEntry

object ClipboardUtils {

    suspend fun copyText(clipboard: Clipboard, text: String) {
        val clipData = ClipData.newPlainText(text, text)
        clipboard.setClipEntry(clipData.toClipEntry())
    }

}