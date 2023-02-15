package greenway_myanmar.org.features.fishfarmrecord.domain.model.season

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Loan
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class Season(
    val id: String,
    val farmMeasurement: FarmMeasurement,
    val name: String,
    val startDate: Instant,
    val fishes: List<Fish>,
    val company: ContractFarmingCompany? = null,
    val loan: Loan? = null,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val isHarvested: Boolean
)