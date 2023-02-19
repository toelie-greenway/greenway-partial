package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FishSize
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFishSize
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductionPerFishSize(
    val size: String,
    val weight: Double,
    val price: Double
)

fun NetworkProductionPerFishSize.asDomainModel() = ProductionPerFishSize(
    fishSize = FishSize.fromString(size),
    weight = weight.toBigDecimalOrZero(),
    price = price.toBigDecimalOrZero()
)
