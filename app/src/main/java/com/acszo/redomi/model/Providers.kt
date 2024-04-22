package com.acszo.redomi.model

import kotlinx.serialization.Serializable

@Serializable
data class Providers(
    val linksByPlatform: Map<String, Link>,
    val entitiesByUniqueId: Map<String, SongInfo>
)

@Serializable
data class Link(
    val url: String
)

@Serializable
data class SongInfo(
    val type: String,
    val title: String,
    val artistName: String,
    val thumbnailUrl: String
)