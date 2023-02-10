package greenway_myanmar.org.util.extensions

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import greenway_myanmar.org.R
import greenway_myanmar.org.util.NetworkManager
import java.math.BigDecimal
import java.util.*

fun String?.toIntOrZero(): Int {
    return this?.toIntOrNull() ?: 0
}

fun String?.toBigDecimalOrZero(): BigDecimal {
    return this?.toBigDecimalOrNull() ?: BigDecimal.ZERO
}

fun BigDecimal?.isNullOrZero(): Boolean {
    return this == null || this == BigDecimal.ZERO
}

fun BigDecimal?.orZero(): BigDecimal {
    return this ?: BigDecimal.ZERO
}

fun Double?.toBigDecimalOrNull(): BigDecimal? {
    return if (this != null) BigDecimal(this.toString()) else null
}

fun Double?.toBigDecimalOrZero(): BigDecimal {
    return if (this != null) BigDecimal(this.toString()) else BigDecimal.ZERO
}


fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun requireNetworkConnection(context: Context, block: () -> Unit) {
    if (NetworkManager.isOnline(context)) {
        block()
    } else {
        Toast.makeText(context, R.string.error_no_network, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.requireNetworkConnection(block: () -> Unit) {
    requireNetworkConnection(requireContext(), block)
}
