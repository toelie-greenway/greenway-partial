package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFish
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductionPerFishType(
    val fish: NetworkFish,
    val production: List<NetworkProductionPerFishSize>
)

fun NetworkProductionPerFishType.asDomainModel() = ProductionPerFish(
    fish = fish.asDomainModel(),
    productionsPerFishSize = production.map {
       it.asDomainModel()
    }
)