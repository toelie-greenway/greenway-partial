package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal

data class CropIncomeSummary(
    val totalIncome: BigDecimal,
    val cropIncomes: List<CropIncome>
)