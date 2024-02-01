package com.acszo.redomi.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.acszo.redomi.utils.IntentUtil

class GeneralLinkActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val generalLink = "https://song.link/${intent?.getStringExtra(Intent.EXTRA_TEXT)}"

        IntentUtil.onIntentSend(this@GeneralLinkActivity, generalLink) { this.finish() }

    }
}