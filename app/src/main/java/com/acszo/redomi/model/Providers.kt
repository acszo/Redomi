package com.acszo.redomi.model

data class Providers(
    val linksByPlatform: Map<String, Link>,
    val entitiesByUniqueId: Map<String, SongInfo>
)

data class Link(
    val url: String
)

data class SongInfo(
    val title: String,
    val artistName: String,
    val thumbnailUrl: String
)