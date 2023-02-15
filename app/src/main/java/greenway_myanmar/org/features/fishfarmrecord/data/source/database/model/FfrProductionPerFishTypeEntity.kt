package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFish
import kotlinx.serialization.Serializable

@Serializable
data class FfrProductionPerFishTypeEntity(
    val fish: FfrFishEntity,
    val productions: List<FfrProductionPerFishSizeEntity>
)

fun FfrProductionPerFishTypeEntity.asDomainModel() = ProductionPerFish(
    fish = fish.asDomainModel(),
    productionsPerFishSize = productions.map(FfrProductionPerFishSizeEntity::asDomainModel)
)