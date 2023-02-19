package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import kotlinx.datetime.Instant

data class UiFcrRecord(
    val id: String,
    val date: Instant,
    val ratios: List<UiFcr>
) {
    companion object {
        fun fromDomainModel(domainModel: FcrRecord) = UiFcrRecord(
            id = domainModel.id,
            date = domainModel.date,
            ratios = domainModel.ratios.map {
                UiFcr.fromDomainModel(it)
            }
        )
    }
}