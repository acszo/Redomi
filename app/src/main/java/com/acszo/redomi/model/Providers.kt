package com.acszo.redomi.model

import kotlinx.serialization.Serializable

@Serializable
data class Providers(
    val entityUniqueId: String,
    val linksByPlatform: Map<String, Link>,
    val entitiesByUniqueId: Map<String, Song>
)

@Serializable
data class Link(
    val url: String,
    val entityUniqueId: String
)

@Serializable
data class Song(
    val isMatched: Boolean = true,
    val platform: String = "",
    val link: String = "",
    val type: String?,
    val title: String?,
    val artistName: String?,
    val thumbnailUrl: String?
)