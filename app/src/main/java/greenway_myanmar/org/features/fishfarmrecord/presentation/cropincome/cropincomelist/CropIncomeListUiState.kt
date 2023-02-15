package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.cropincomelist

import java.math.BigDecimal
import java.time.Instant

data class CropIncomeListUiState(
    val id: String,
    val cropName: String,
    val income: BigDecimal,
    val date: Instant
)