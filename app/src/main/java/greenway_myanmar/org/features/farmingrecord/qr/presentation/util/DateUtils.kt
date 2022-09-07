package greenway_myanmar.org.features.farmingrecord.qr.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

object DateUtils {

    fun format(date: Instant, pattern: String): String {
        val zoneId = ZoneId.systemDefault()
        val locale = Locale("my")
        return DateTimeFormatter.ofPattern(pattern)
            .withLocale(locale)
            .withDecimalStyle(DecimalStyle.of(locale))
            .withZone(zoneId)
            .format(date)
    }
}