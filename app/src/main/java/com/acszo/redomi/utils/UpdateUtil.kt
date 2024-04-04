package com.acszo.redomi.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Build
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object UpdateUtil {

    fun Context.getApk() = File(getExternalFilesDir("apk"), "latest-release")

    private fun apkUri(context: Context) = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        context.getApk()
    )

    suspend fun installApk(context: Context) = withContext(Dispatchers.IO) {
        val installer = context.packageManager.packageInstaller
        val resolver = context.contentResolver

        val apkUri = apkUri(context)
        val apkSize = resolver.openAssetFileDescriptor(apkUri, "r")?.use { it.length } ?: -1L

        resolver.openInputStream(apkUri)?.use { apkStream ->

            val sessionParams = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                sessionParams.setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED)
            }

            val sessionId = installer.createSession(sessionParams)
            val session = installer.openSession(sessionId)

            session.openWrite(context.packageName, 0, apkSize).use { sessionStream ->
                apkStream.copyTo(sessionStream)
                session.fsync(sessionStream)
            }

            val intentSender = PendingIntent.getBroadcast(
                context,
                sessionId,
                Intent(context, UpdateReceiver::class.java),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            ).intentSender

            session.commit(intentSender)
            session.close()
        }
    }

    fun deleteApk(context: Context) = context.runCatching {
        val apk = context.getApk()
        if (apk.exists()) {
            apk.delete()
        }
    }

}