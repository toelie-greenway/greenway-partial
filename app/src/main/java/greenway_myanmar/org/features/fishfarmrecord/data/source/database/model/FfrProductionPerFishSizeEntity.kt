package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import com.greenwaymyanmar.core.data.json.serializers.BigDecimalAsString
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FishSize
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFishSize
import kotlinx.serialization.Serializable

@Serializable
data class FfrProductionPerFishSizeEntity(
    val size: String,
    val weight: BigDecimalAsString,
    val price: BigDecimalAsString
)

fun FfrProductionPerFishSizeEntity.asDomainModel() = ProductionPerFishSize(
    fishSize = FishSize.fromString(size),
    weight = weight,
    price = price
)