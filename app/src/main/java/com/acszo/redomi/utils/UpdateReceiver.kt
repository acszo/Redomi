package com.acszo.redomi.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.widget.Toast
import com.acszo.redomi.R
import com.acszo.redomi.utils.IntentUtil.onIntentSettingsPage
import com.acszo.redomi.utils.UpdateUtil.deleteApk

class UpdateReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, R.string.updating, Toast.LENGTH_LONG).show()
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -1)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                val userAction = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                userAction?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(userAction)
            }
            PackageInstaller.STATUS_FAILURE_ABORTED -> {
                Toast.makeText(context, R.string.update_aborted, Toast.LENGTH_LONG).show()
            }
            PackageInstaller.STATUS_SUCCESS -> {
                Toast.makeText(context, R.string.update_completed, Toast.LENGTH_LONG).show()
                onIntentSettingsPage(context)
                deleteApk(context)
            }
            else -> {
                Toast.makeText(context, R.string.update_failed, Toast.LENGTH_LONG).show()
            }
        }
    }

}