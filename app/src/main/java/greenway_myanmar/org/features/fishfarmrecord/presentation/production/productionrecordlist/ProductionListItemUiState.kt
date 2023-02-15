package greenway_myanmar.org.features.fishfarmrecord.presentation.production.productionrecordlist

import java.math.BigDecimal
import java.time.Instant

data class ProductionListItemUiState(
    val id: String,
    val date: Instant,
    val note: String,
    val totalWeight: BigDecimal
)