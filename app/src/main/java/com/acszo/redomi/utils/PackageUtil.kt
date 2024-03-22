package com.acszo.redomi.utils

import android.content.Context
import android.content.pm.PackageManager

// not used anymore, keeping it cuz why not ;(
object PackageUtil {

    private fun isInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun isPackagePresent(context: Context, packageNames: List<String>): Boolean  {
        var isInstalled = false
        for (packageName in packageNames) {
            if (isInstalled(context, packageName)) isInstalled = true
        }
        return isInstalled
    }

}