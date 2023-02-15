package greenway_myanmar.org.features.fishfarmrecord.domain.model

import kotlinx.datetime.Instant
import java.math.BigDecimal

data class ProductionRecord(
    val id: String,
    val date: Instant,
    val productions: List<ProductionPerFish>,
    val note: String
) {
    val totalPrice: BigDecimal = productions.sumOf { productionPerFish ->
        productionPerFish.productionsPerFishSize.sumOf { it.price * it.weight }
    }
    val totalWeight: BigDecimal = productions.sumOf { productionPerFish ->
        productionPerFish.productionsPerFishSize.sumOf { it.weight }
    }
}