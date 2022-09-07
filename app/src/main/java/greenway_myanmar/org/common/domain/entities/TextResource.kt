package greenway_myanmar.org.common.domain.entities

import android.content.Context
import androidx.annotation.StringRes
import java.util.*

sealed class Text {
    data class StringText(val text: String) : Text()

    data class ResourceText(@StringRes val resId: Int) : Text()

    data class ResourceFormattedText(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : Text()
}

fun Text.asString(context: Context, supportZawgyi: Boolean = true): String {
    val text =
        when (this) {
            is Text.StringText -> text
            is Text.ResourceText -> context.getString(resId)
            is Text.ResourceFormattedText -> context.getString(resId, *args.toTypedArray())
        }
    return if (supportZawgyi) {
        text
    } else {
        text
    }
}
