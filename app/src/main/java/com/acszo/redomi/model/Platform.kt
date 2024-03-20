package com.acszo.redomi.model

import com.acszo.redomi.R

object Platform {

    val platforms: List<AppDetails> = listOf(
        AppDetails("youtubeMusic", R.drawable.ic_youtube_music, listOf("com.google.android.apps.youtube.music", "app.revanced.android.apps.youtube.music", "app.rvx.android.apps.youtube.music", "com.zionhuang.music", "it.vfsfitvnm.vimusic")),
        AppDetails("youtube", R.drawable.ic_youtube, listOf("com.google.android.youtube", "app.revanced.android.youtube", "app.rvx.android.youtube", "org.schabi.newpipe" , "com.github.libretube")),
        AppDetails("spotify", R.drawable.ic_spotify, listOf("com.spotify.music")),
        AppDetails("deezer", R.drawable.ic_deezer, listOf("deezer.android.app")),
        AppDetails("tidal", R.drawable.ic_tidal, listOf("com.aspiro.tidal")),
        AppDetails("amazonMusic", R.drawable.ic_amazon_music, listOf("com.amazon.mp3")),
        AppDetails("appleMusic", R.drawable.ic_apple_music, listOf("com.apple.android.music")),
        AppDetails("soundcloud", R.drawable.ic_soundcloud, listOf("com.soundcloud.android")),
        AppDetails("napster", R.drawable.ic_napster, listOf("com.rhapsody.napster")),
    )

}