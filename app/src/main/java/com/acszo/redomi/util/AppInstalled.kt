package com.acszo.redomi.util

import android.content.pm.PackageManager

class AppInstalled {

    fun isInstalled(packageManager: PackageManager, packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

}