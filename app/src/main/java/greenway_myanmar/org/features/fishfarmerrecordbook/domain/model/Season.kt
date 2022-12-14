package greenway_myanmar.org.features.fishfarmerrecordbook.domain.model

import java.math.BigDecimal

data class Season(
    val id: String,
    val name: String,
    val totalExpenses: BigDecimal,
    val contractFarmingCompany: ContractFarmingCompany? = null
)