package greenway_myanmar.org.features.fishfarmrecord.domain.model.season

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import java.math.BigDecimal
import java.time.Instant

data class Season(
    val id: String,
    val name: String,
    val totalExpenses: BigDecimal,
    val contractFarmingCompany: ContractFarmingCompany? = null,
    val startDate: Instant
)