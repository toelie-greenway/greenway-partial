package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import com.greenwaymyanmar.core.data.json.serializers.BigDecimalAsString
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecordSummary
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.toInstantOrNow
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductionRecordSummary(
    val total_weight: BigDecimalAsString? = null,
    val total_income: BigDecimalAsString? = null,
    val last_production_date: String? = null,
    val productions: List<NetworkProductionRecord>? = null
)

fun NetworkProductionRecordSummary.asDomainModel() = ProductionRecordSummary(
    totalWeight = total_weight.orZero(),
    totalIncome = total_income.orZero(),
    lastRecordDate = last_production_date.toInstantOrNow(),
    productionRecords = productions.orEmpty().map {
        it.asDomainModel()
    }
)