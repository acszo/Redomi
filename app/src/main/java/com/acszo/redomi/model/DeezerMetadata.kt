package com.acszo.redomi.model

import kotlinx.serialization.Serializable

@Serializable
data class DeezerMetadata(
    val id: Long? = null,
    val isrc: String? = null,
    val upc: String? = null,
    val error: DeezerError? = null
)

@Serializable
data class DeezerError(val code: Int)
