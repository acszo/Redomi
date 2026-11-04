package com.acszo.redomi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.acszo.redomi.data.DataStoreConst.COUNTRY_CODE
import com.acszo.redomi.data.DataStoreConst.DEFAULT_COUNTRY_CODE
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.QobuzLink
import com.acszo.redomi.repository.SongLinkRepository
import com.acszo.redomi.service.ApiResult
import com.acszo.redomi.ui.page.bottom_sheet.components.BottomSheetError
import com.acszo.redomi.ui.theme.RedomiTheme
import com.acszo.redomi.utils.IntentUtil
import com.acszo.redomi.utils.SharedLink
import com.acszo.redomi.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UniversalLinkActivity: ComponentActivity() {
    @Inject lateinit var songLinkRepository: SongLinkRepository
    @Inject lateinit var settingsDataStore: SettingsDataStore
    private var error by mutableStateOf<Int?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = SharedLink.extract(intent.getStringExtra(Intent.EXTRA_TEXT))
        if (url == null) {
            Toast.makeText(this, R.string.error_incorrect_url, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        if (!QobuzLink.isQobuzUrl(url)) {
            IntentUtil.onIntentSend(this, "https://song.link/$url")
            finish()
            return
        }

        setContent {
            val settings: DataStoreViewModel = hiltViewModel()
            val theme by settings.themeMode.collectAsStateWithLifecycle()
            RedomiTheme(theme = theme) {
                ModalBottomSheet(onDismissRequest = { finish() }) {
                    val message = error
                    if (message != null) {
                        BottomSheetError(message = stringResource(message), onClick = { resolve(url) })
                    } else {
                        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
        resolve(url)
    }

    private fun resolve(url: String) = lifecycleScope.launch {
        error = null
        val country = settingsDataStore.getString(COUNTRY_CODE).first() ?: DEFAULT_COUNTRY_CODE
        when (val response = songLinkRepository.getSongs(url, country, includeQobuz = false)) {
            is ApiResult.Success -> {
                val universalLink = response.data.pageUrl
                if (universalLink.isNullOrBlank()) {
                    error = R.string.error_no_universal_link
                } else {
                    IntentUtil.onIntentSend(this@UniversalLinkActivity, universalLink)
                    finish()
                }
            }
            is ApiResult.Error -> {
                error = if (response.code in 400..499) R.string.error_incorrect_url else R.string.error_server
            }
            is ApiResult.Exception -> error = response.message
        }
    }
}