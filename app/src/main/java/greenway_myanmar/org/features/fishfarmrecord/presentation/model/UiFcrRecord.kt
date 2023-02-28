package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import greenway_myanmar.org.util.DateUtils
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant

data class UiFcrRecord(
    val id: String,
    val date: Instant,
    val ratios: List<UiFcr>
) {
    val formattedDate = DateUtils.format(date.toJavaInstant(), "dd MMMM၊ yyyy")

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