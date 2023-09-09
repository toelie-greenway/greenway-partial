package com.greenwaymyanmar.common.feature.tag.data.source.network.model

@kotlinx.serialization.Serializable
class NetworkStatusResponse(
    val status_code: Int,
    val message: String,
    val success: Boolean
)