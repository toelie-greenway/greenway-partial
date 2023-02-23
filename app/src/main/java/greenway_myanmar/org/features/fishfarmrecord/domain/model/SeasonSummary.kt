package greenway_myanmar.org.features.fishfarmrecord.domain.model

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
    val familyCost: BigDecimal,
    val expenseSummary: ExpenseSummary?,
    val productionRecordSummary: ProductionRecordSummary?,
    val cropIncomeSummary: CropIncomeSummary?,
    val fcrRecords: List<FcrRecord>?,
    val totalExpenses: BigDecimal,
    val totalIncomes: BigDecimal,
    val totalProfit: BigDecimal
) {
    val isProfit = totalProfit > BigDecimal.ZERO
}