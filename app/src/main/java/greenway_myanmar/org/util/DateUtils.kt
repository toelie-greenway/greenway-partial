package greenway_myanmar.org.util

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.DecimalStyle
import java.util.*

object DateUtils {

    fun prepareServerDateFromInstant(date: Instant): String {
        val zoneId = ZoneId.systemDefault()
        val locale = Locale.US
        return DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withLocale(locale)
            .withDecimalStyle(DecimalStyle.of(locale))
            .withZone(zoneId)
            .format(date)
    }

    fun format(date: Instant, pattern: String): String {
        val zoneId = ZoneId.systemDefault()
        val locale = Locale("my")
        return DateTimeFormatter.ofPattern(pattern)
            .withLocale(locale)
            .withDecimalStyle(DecimalStyle.of(locale))
            .withZone(zoneId)
            .format(date)
    }

    fun parseDateToInstantOrNull(string: String?): Instant? {
        if (string.isNullOrEmpty()) return null

        return try {
            val localDate: LocalDate = LocalDate.parse(string)
            val localDateTime: LocalDateTime = localDate.atStartOfDay()
            return localDateTime.toInstant(ZoneOffset.UTC)
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

    fun parseServerDateTimeToInstantOrNow(string: String?): Instant {
        if (string.isNullOrEmpty()) return Instant.now(Clock.systemUTC())

        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            LocalDateTime.parse(string, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
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

    fun parseServerDateTimeToInstantOrNull(string: String?): Instant? {
        if (string.isNullOrEmpty()) return null

        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            LocalDateTime.parse(string, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
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
}


fun String?.toInstantOrNow(): kotlinx.datetime.Instant {
    return DateUtils.parseServerDateTimeToInstantOrNow(this).toKotlinInstant()
}

fun String?.toInstantOrNull(): kotlinx.datetime.Instant? {
    return DateUtils.parseServerDateTimeToInstantOrNull(this)?.toKotlinInstant()
}

fun kotlinx.datetime.Instant.toServerDateString(): String {
    return DateUtils.prepareServerDateFromInstant(this.toJavaInstant())
}

fun LocalDate.toKotlinInstant() = this.atStartOfDay().toInstant(ZoneOffset.UTC).toKotlinInstant()