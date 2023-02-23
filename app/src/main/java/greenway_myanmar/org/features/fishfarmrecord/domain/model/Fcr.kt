package greenway_myanmar.org.features.fishfarmrecord.domain.model

import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

data class Fcr(
    val fish: Fish,
    val feedWeight: BigDecimal,
    val gainWeight: BigDecimal,
) {
    val ratio: BigDecimal = try {
        Timber.d("Ratio: $feedWeight / $gainWeight = ${feedWeight.divide(gainWeight, 2, RoundingMode.HALF_UP)}")
        gainWeight.divide(feedWeight, 2, RoundingMode.HALF_UP)
    } catch (e: ArithmeticException) {
        BigDecimal.ZERO
    }
}