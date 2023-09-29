package com.acszo.redomi.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.FileProvider
import java.io.File

object PackageUtil {

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun installApk(context: Context, apkName: String) {
        val latestApkUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File(context.getExternalFilesDir("apk"), apkName)
        )
        IntentUtil.onIntentPackageInstaller(context, latestApkUri)
    }

}