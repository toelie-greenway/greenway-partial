package greenway_myanmar.org.util.extensions

import java.math.BigDecimal
import java.util.*

fun String?.toBigDecimalOrZero(): BigDecimal {
    return this?.toBigDecimalOrNull() ?: BigDecimal.ZERO
}

fun BigDecimal?.isNullOrZero(): Boolean {
    return this == null || this == BigDecimal.ZERO
}

fun BigDecimal?.orZero(): BigDecimal {
    return this ?: BigDecimal.ZERO
}

fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}