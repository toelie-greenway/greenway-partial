package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductionPerFishSize(
    val size: String,
    val weight: Double,
    val price: Double
)