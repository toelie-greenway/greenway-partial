package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSeasonEndReason(
    val id: String? = null,
    val reason: String? = null,
    val order: Int? = -1
)

fun NetworkSeasonEndReason.asDomainModel() = SeasonEndReason(
    id = id.orEmpty(),
    name = reason.orEmpty()
)