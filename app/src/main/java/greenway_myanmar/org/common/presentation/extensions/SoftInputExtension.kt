package greenway_myanmar.org.common.presentation.extensions

import android.app.Activity
import android.content.Context
import android.os.ResultReceiver
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

fun Fragment.showSoftInput() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    try {
        val showSoftInputUnchecked = InputMethodManager::class.java.getMethod(
            "showSoftInputUnchecked", Int::class.javaPrimitiveType, ResultReceiver::class.java
        )
        showSoftInputUnchecked.isAccessible = true
        showSoftInputUnchecked.invoke(imm, 0, null)
    } catch (e: Exception) {
        // no-op
    }
}

fun Activity.hideSoftInput() {
    val imm: InputMethodManager? = getSystemService()
    val currentFocus = currentFocus
    if (currentFocus != null && imm != null) {
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Fragment.hideSoftInput() = requireActivity().hideSoftInput()

fun View.hideSoftInput() {
    val imm: InputMethodManager? = this.context.getSystemService()
    imm?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextView.showIme() {
    val imm: InputMethodManager? = this.context.getSystemService()
    imm?.showSoftInput(this, 0, null)
}

//private fun EditText.createShowImeRunnable() = Runnable {
//    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//    imm?.showSoftInput(this, 0)
//}
//
//fun EditText.handleImeVisibility() {
//    val runnable = createShowImeRunnable()
//    onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
//        if (hasFocus) {
//            post(runnable)
//        } else {
//            removeCallbacks(runnable)
//            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.hideSoftInputFromWindow(v.windowToken, 0)
//        }
//    }
//}