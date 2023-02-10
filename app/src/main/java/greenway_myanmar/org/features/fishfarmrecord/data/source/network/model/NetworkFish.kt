package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkFish(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null
)