package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecord
import kotlinx.datetime.toJavaInstant
import java.math.BigDecimal
import java.time.Instant

data class UiProductionRecord(
    val id: String,
    val date: Instant,
    val totalPrice: BigDecimal,
    val totalWeight: BigDecimal,
    val note: String
) {
    companion object {
        fun fromDomainModel(domainModel: ProductionRecord) = UiProductionRecord(
            id = domainModel.id,
            date = domainModel.date.toJavaInstant(),
            totalPrice = domainModel.totalPrice,
            totalWeight = domainModel.totalWeight,
            note = domainModel.note
        )
    }
}