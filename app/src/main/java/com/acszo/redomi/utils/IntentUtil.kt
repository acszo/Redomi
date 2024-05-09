package com.acszo.redomi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.acszo.redomi.MainActivity

object IntentUtil {

    fun onIntentView(context: Context, url: String) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(uri)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // checks which app is the default to open the uri in question with MATCH_DEFAULT_ONLY
        val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val packageName = resolveInfo?.activityInfo?.packageName ?: ""

        if (packageName != context.packageName) {
            context.startActivity(intent)
        } else {
            val browserIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
                .setData(uri)
            context.startActivity(browserIntent)
        }
    }

    fun onIntentSend(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, url)
            .setType("text/plain")
        context.startActivity(Intent.createChooser(intent, null))
    }

    @SuppressLint("InlinedApi")
    fun onIntentOpenDefaultsApp(context: Context) {
        val uri = Uri.parse("package:${context.packageName}")
        // Work around for One UI 4, because ACTION_APP_OPEN_BY_DEFAULT_SETTING crashes, kpop company moment 🫰
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S && Build.MANUFACTURER.equals("samsung", ignoreCase = true)) {
            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri))
        } else {
            context.startActivity(Intent(Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS, uri))
        }
    }

    fun onIntentSettingsPage(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

}