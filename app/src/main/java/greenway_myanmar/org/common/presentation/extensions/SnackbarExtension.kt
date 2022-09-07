package greenway_myanmar.org.common.presentation.extensions

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString


fun Fragment.showSnackbar(text: Text, longDuration: Boolean = true) {
    val contentView = activity?.findViewById<View>(android.R.id.content)
    if (contentView != null) {
        Snackbar.make(
            contentView,
            text.asString(requireContext()),
            if (longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
        ).show()
    } else {
        // just in case if can't get content view
        Toast.makeText(
            context,
            text.asString(requireContext()),
            if (longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()
    }
}