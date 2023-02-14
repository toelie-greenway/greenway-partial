package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkFcrRecord(
    val id: String,
    val date: String? = null,
    val record: List<NetworkFcr>? = null
)