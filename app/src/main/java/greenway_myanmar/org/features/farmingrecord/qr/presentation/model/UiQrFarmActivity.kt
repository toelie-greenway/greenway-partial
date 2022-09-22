package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

data class UiQrFarmActivity(
    val activityName: String,
    val date: Instant,
    val farmInputs: String
) {
    val formattedDate: String
        get() = DateUtils.format(date, "d MMMM, yyyy")

    companion object {
        fun fromDomain(domainEntity: QrFarmActivity) = UiQrFarmActivity(
            activityName = domainEntity.activityName,
            date = domainEntity.date,
            farmInputs = domainEntity.farmInputs.orEmpty()
        )
    }
}