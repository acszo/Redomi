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
        val uri = Uri.parse(url)
        var intent = Intent(Intent.ACTION_VIEW)
            .setData(uri)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // Checks the default app that resolves the uri
        val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val packageName = resolveInfo?.activityInfo?.packageName ?: ""

        // Force to open in browser, when Redomi is the default to resolve the uri
        if (packageName == context.packageName) {
            intent = Intent.makeMainSelectorActivity(
                Intent.ACTION_MAIN,
                Intent.CATEGORY_APP_BROWSER
            ).setData(uri)
        }
        context.startActivity(intent)
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
        val settings = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S && Build.MANUFACTURER.equals("samsung", ignoreCase = true)) {
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        } else {
            Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS
        }
        context.startActivity(Intent(settings, uri))
    }

    fun onIntentSettingsPage(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

}