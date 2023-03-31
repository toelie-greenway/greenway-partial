package greenway_myanmar.org.features.thread.presentation.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.util.extensions.dp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ThreadTagVotedListDividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1.dp(context).toInt())
        setBackgroundColor(ContextCompat.getColor(context, R.color.app_divider))
    }

}