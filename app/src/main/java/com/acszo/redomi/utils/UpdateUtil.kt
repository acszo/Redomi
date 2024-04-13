package com.acszo.redomi.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object UpdateUtil {

    private const val MIME_APK = "application/vnd.android.package-archive"

    fun Context.getApk() = File(getExternalFilesDir("apk"), "latest-release")

    private fun apkUri(context: Context) = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        context.getApk()
    )

    // much shorter than my previous PackageInstaller solution, but not safe.
    // Fellas hope that I don't get hacked 👍
    fun installApk(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setDataAndType(apkUri(context), MIME_APK)
        context.startActivity(intent)
    }

    fun deleteApk(context: Context) = context.runCatching {
        val apk = context.getApk()
        if (apk.exists()) {
            apk.delete()
        }
    }

}