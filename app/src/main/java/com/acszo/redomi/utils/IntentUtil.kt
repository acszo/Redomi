package com.acszo.redomi.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentUtil {

    fun onIntentView(context: Context, url: String) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun onIntentSend(context: Context, url: String, onDismiss: () -> Unit) {
        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, url)
            .setType("text/plain")
        context.startActivity(Intent.createChooser(intent, null))
        onDismiss()
    }

}