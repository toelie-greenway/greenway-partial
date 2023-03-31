package greenway_myanmar.org.features.thread.presentation.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.ThreadTagVoteButtonViewBinding
import greenway_myanmar.org.util.extensions.dp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ThreadTagVoteButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ThreadTagVoteButtonViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        setPadding(0, 8.dp(context).toInt(), 0, 16.dp(context).toInt())
    }

    @CallbackProp
    fun setClickCallback(listener: OnClickListener?) {
        binding.goToVoteButton.setOnClickListener {
            listener?.onClick(it)
        }
    }
}