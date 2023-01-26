package greenway_myanmar.org.util.extensions

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun EditText.bindMoney(value: BigDecimal?) {
    if (value == null) {
        bindText("")
    } else {
        val currencyFormat: NumberFormat = NumberFormat.getInstance(Locale.US)
        bindText(currencyFormat.format(value))
    }
}

fun EditText.bindText(text: String?) {
    val newValue = text ?: ""
    val oldValue = this.text.toString()
    if (newValue != oldValue) {
        val currentCursorPosition = selectionEnd
        setText(newValue)
        setSelection(calculateCursorPosition(oldValue, newValue, currentCursorPosition))
    }
}

fun EditText.calculateCursorPosition(
    oldText: String?,
    text: String?,
    currentCursorPosition: Int,
): Int {
    val newValue = text ?: ""
    val oldValue = oldText ?: ""
    val oldValueLength = oldValue.length
    val newValueLength = newValue.length
    val newCursorPosition = if (oldValueLength == newValueLength) {
        currentCursorPosition
    } else if (newValueLength > oldValueLength) {
        currentCursorPosition + (newValueLength - oldValueLength)
    } else {
        currentCursorPosition - (oldValueLength - newValueLength)
    }

    return if (newCursorPosition >= 0) {
        newCursorPosition
    } else {
        0
    }
}


fun TextInputLayout.setError(errorText: Text? = null) {
    if (errorText == null) {
        error = null
        isErrorEnabled = false
    } else {
        isErrorEnabled = true
        error = errorText.asString(context)
    }
}

