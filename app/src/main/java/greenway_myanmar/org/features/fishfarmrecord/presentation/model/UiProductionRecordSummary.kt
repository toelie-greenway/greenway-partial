package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecordSummary
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class UiProductionRecordSummary(
    val totalWeight: BigDecimal,
    val totalIncome: BigDecimal,
    val lastRecordDate: Instant,
    val productionRecords: List<UiProductionRecord>
) {
    companion object {
        fun fromDomainModel(domainModel: ProductionRecordSummary) = UiProductionRecordSummary(
            totalWeight = domainModel.totalWeight,
            totalIncome = domainModel.totalIncome,
            lastRecordDate = domainModel.lastRecordDate,
            productionRecords = domainModel.productionRecords.map {
                UiProductionRecord.fromDomainModel(it)
            }
        )
    }
}