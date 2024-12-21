package com.acszo.redomi.model

import com.acszo.redomi.R
import com.acszo.redomi.service.Api.SEARCH_QUERY_YT_MUSIC
import com.acszo.redomi.service.Api.SEARCH_QUERY_YOUTUBE
import com.acszo.redomi.service.Api.SEARCH_QUERY_SPOTIFY
import com.acszo.redomi.service.Api.SEARCH_QUERY_DEEZER
import com.acszo.redomi.service.Api.SEARCH_QUERY_TIDAL
import com.acszo.redomi.service.Api.SEARCH_QUERY_AMAZON_MUSIC
import com.acszo.redomi.service.Api.SEARCH_QUERY_APPLE_MUSIC
import com.acszo.redomi.service.Api.SEARCH_QUERY_SOUNDCLOUD
import com.acszo.redomi.service.Api.SEARCH_QUERY_NAPSTER
import com.acszo.redomi.service.Api.SEARCH_QUERY_YANDEX
import com.acszo.redomi.service.Api.SEARCH_QUERY_ANGHAMI

object Platform {

    val platforms = mapOf(
        "youtubeMusic" to AppDetails("Youtube Music", R.drawable.ic_youtube_music, SEARCH_QUERY_YT_MUSIC),
        "youtube" to AppDetails("Youtube", R.drawable.ic_youtube, SEARCH_QUERY_YOUTUBE),
        "spotify" to AppDetails("Spotify", R.drawable.ic_spotify, SEARCH_QUERY_SPOTIFY),
        "deezer" to AppDetails("Deezer", R.drawable.ic_deezer, SEARCH_QUERY_DEEZER),
        "tidal" to AppDetails("Tidal", R.drawable.ic_tidal, SEARCH_QUERY_TIDAL),
        "amazonMusic" to AppDetails("Amazon Music", R.drawable.ic_amazon_music, SEARCH_QUERY_AMAZON_MUSIC),
        "appleMusic" to AppDetails("Apple Music", R.drawable.ic_apple_music, SEARCH_QUERY_APPLE_MUSIC),
        "soundcloud" to AppDetails("Soundcloud", R.drawable.ic_soundcloud, SEARCH_QUERY_SOUNDCLOUD),
        "napster" to AppDetails("Napster", R.drawable.ic_napster, SEARCH_QUERY_NAPSTER),
        "yandex" to AppDetails("Yandex Music", R.drawable.ic_yandex, SEARCH_QUERY_YANDEX),
        "anghami" to AppDetails("Anghami", R.drawable.ic_anghami, SEARCH_QUERY_ANGHAMI)
    )

}