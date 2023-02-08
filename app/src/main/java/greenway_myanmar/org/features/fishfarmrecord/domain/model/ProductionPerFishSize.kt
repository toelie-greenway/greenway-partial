package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal

data class ProductionPerFishSize(
    val fishSize: FishSize,
    val weight: BigDecimal,
    val price: BigDecimal
)