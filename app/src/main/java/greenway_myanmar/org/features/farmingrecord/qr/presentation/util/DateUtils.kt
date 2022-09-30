package greenway_myanmar.org.features.farmingrecord.qr.presentation.util

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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


    fun parseIsoDateTimeToInstantOrNull(string: String?): Instant? {
        if (string.isNullOrEmpty()) return null

        return try {
            Instant.parse(string)
        } catch (e: NullPointerException) {
            null
        } catch (e: DateTimeParseException) {
            null
        } catch (e: RuntimeException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    fun parseIsoDateTimeToInstantOrDefault(string: String?): Instant {
        if (string.isNullOrEmpty()) return Instant.now(Clock.systemUTC())

        return try {
            Instant.parse(string)
        } catch (e: NullPointerException) {
            Instant.now(Clock.systemUTC())
        } catch (e: DateTimeParseException) {
            Instant.now(Clock.systemUTC())
        } catch (e: RuntimeException) {
            Instant.now(Clock.systemUTC())
        } catch (e: Exception) {
            Instant.now(Clock.systemUTC())
        }
    }
}