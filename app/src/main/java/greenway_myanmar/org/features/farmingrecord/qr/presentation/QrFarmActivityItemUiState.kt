package greenway_myanmar.org.features.farmingrecord.qr.presentation

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
        get() = dateFormatted(date)

    private fun dateFormatted(date: Instant): String {
        val zoneId = ZoneId.systemDefault()
        val locale = Locale("my")// LocaleListCompat.getDefault().get(0)
        return DateTimeFormatter.ofPattern("d MMMM, yyyy")
            .withLocale(locale)
            .withDecimalStyle(DecimalStyle.of(locale))
            .withZone(zoneId)
            .format(date)
    }

}