package com.acszo.redomi.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.FileProvider
import java.io.File

object PackageUtil {

    fun Context.getApk() = File(getExternalFilesDir("apk"), "latest-release")

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun installApk(context: Context) {
        val latestApkUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            context.getApk()
        )
        IntentUtil.onIntentPackageInstaller(context, latestApkUri)
    }

    fun deleteApk(context: Context) = context.runCatching {
        val apk = context.getApk()
        if (apk.exists()) {
            apk.delete()
        }
    }

}