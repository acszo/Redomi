package com.acszo.redomi.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.acszo.redomi.MainActivity

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

    fun onIntentDefaultsApp(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.startActivity(
                Intent(
                    Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                    Uri.parse("package:${context.packageName}")
                )
            )
        } else {
            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${context.packageName}")
                )
            )
        }
    }

    fun onIntentSettingsPage(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

}