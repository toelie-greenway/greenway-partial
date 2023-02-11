package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkSeasonEndReason(
    val id: String? = null,
    val reason: String? = null,
    val order: Int? = -1
)
