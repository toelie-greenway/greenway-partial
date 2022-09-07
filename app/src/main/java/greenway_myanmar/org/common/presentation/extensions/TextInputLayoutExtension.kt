package greenway_myanmar.org.common.presentation.extensions

import com.google.android.material.textfield.TextInputLayout
import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString

fun TextInputLayout.setTextError(error: Text?) {
    if (error != null) {
        isErrorEnabled = true
        setError(error.asString(context))
    } else {
        setError(null)
        isErrorEnabled = false
    }
}