package com.acszo.redomi.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.acszo.redomi.utils.IntentUtil

class UniversalLinkActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val universalLink = "https://song.link/${intent?.getStringExtra(Intent.EXTRA_TEXT)}"

        IntentUtil.onIntentSend(this@UniversalLinkActivity, universalLink)
        this.finish()
    }

}