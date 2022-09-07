package greenway_myanmar.org.features.farmingrecord.qr.presentation

import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

data class QrFarmActivityItemUiState(
    val activityName: String,
    val date: Instant,
    val farmInputs: String
) {
    val formattedDate: String
        get() = DateUtils.format(date, "d MMMM, yyyy")

}