package com.acszo.redomi.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Providers(
    val entityUniqueId: String,
    val linksByPlatform: Map<String, Link>,
    val entitiesByUniqueId: Map<String, Song>,
    val pageUrl: String? = null,
    @Transient val lookupFailed: Boolean = false
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
    val thumbnailUrl: String? = null
)