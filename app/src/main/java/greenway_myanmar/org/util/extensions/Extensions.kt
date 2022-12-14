package greenway_myanmar.org.util.extensions

import java.math.BigDecimal

fun String?.toBigDecimalOrZero(): BigDecimal {
    return this?.toBigDecimalOrNull() ?: BigDecimal.ZERO
}

fun BigDecimal?.isNullOrZero(): Boolean {
    return this == null || this == BigDecimal.ZERO
}

fun BigDecimal?.orZero(): BigDecimal {
    return this ?: BigDecimal.ZERO
}
