package greenway_myanmar.org.common.domain.entities

import android.content.Context
import androidx.annotation.StringRes

sealed class Text {
    data class StringText(val text: String) : Text()

    data class ResourceText(@StringRes val resId: Int) : Text()

    data class ResourceFormattedText(
        @StringRes val resId: Int,
        val args: List<String> = emptyList()
    ) : Text()
}

fun Text.string(): String {
    return if (this is Text.StringText) {
        text
    } else {
        ""
    }
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
//        if (!MMFontUtils.isSupportUnicode()) {
//            MMFontUtils2.uni2zg(text)
//        } else {
//            text
//        }
    } else {
        text
    }
}
