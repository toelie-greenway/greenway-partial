package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductionPerFishType(
    val fish: NetworkFish,
    val production: List<NetworkProductionPerFishSize>
)