package greenway_myanmar.org.features.fishfarmrecord.domain.model

import greenway_myanmar.org.util.extensions.orZero
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class SeasonSummary(
    val id: String,
    val farmMeasurement: FarmMeasurement,
    val seasonName: String,
    val farmName: String,
    val startDate: Instant,
    val endDate: Instant,
    val endReason: SeasonEndReason?,
    val isHarvested: Boolean,
    val fishes: List<Fish>,
    val company: ContractFarmingCompany? = null,
    val loan: Loan? = null,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val familyCost: BigDecimal = BigDecimal.ZERO,
    val categoryExpenses: List<CategoryExpense>,
    val productionRecordSummary: ProductionRecordSummary?,
    val cropIncomeSummary: CropIncomeSummary?,
    val fcrRecords: List<FcrRecord>?,
) {
    val totalIncomes = productionRecordSummary?.totalIncome.orZero() + cropIncomeSummary?.totalIncome.orZero()
    val totalProfit = totalIncomes - totalExpenses
    val isProfit = totalProfit > BigDecimal.ZERO
}