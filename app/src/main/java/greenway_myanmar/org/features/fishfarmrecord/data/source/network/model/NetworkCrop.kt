package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCrop(
    val id: String? = null,
    val title: String? = null,
    val image: String? = null
)