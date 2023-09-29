package com.acszo.redomi.model

import com.acszo.redomi.R

object Platform {

    val platforms: List<AppDetails> = listOf(
        AppDetails("youtubeMusic", R.drawable.youtube_music_icon, listOf("com.google.android.apps.youtube.music", "app.revanced.android.apps.youtube.music", "com.zionhuang.music", "it.vfsfitvnm.vimusic")),
        AppDetails("youtube", R.drawable.youtube_icon, listOf("com.google.android.youtube", "app.revanced.android.youtube", "org.schabi.newpipe" , "com.github.libretube")),
        AppDetails("spotify", R.drawable.spotify_icon, listOf("com.spotify.music")),
        AppDetails("deezer", R.drawable.deezer_icon, listOf("deezer.android.app")),
        AppDetails("tidal", R.drawable.tidal_icon, listOf("com.aspiro.tidal")),
        AppDetails("amazonMusic", R.drawable.amazon_music_icon, listOf("com.amazon.mp3")),
        AppDetails("appleMusic", R.drawable.apple_music_icon, listOf("com.apple.android.music")),
        AppDetails("soundcloud", R.drawable.soundcloud_icon, listOf("com.soundcloud.android")),
        AppDetails("napster", R.drawable.napster_icon, listOf("com.rhapsody.napster")),
    )

}