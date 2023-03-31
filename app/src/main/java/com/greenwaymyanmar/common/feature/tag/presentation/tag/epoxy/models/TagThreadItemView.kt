package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagThread
import greenway_myanmar.org.databinding.TagThreadItemViewBinding
import greenway_myanmar.org.util.extensions.load
import kotlinx.datetime.toJavaInstant
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

private val prettyTime = PrettyTime(Date(), Locale("MM"))

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagThreadItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = TagThreadItemViewBinding.inflate(LayoutInflater.from(context), this, true)

    @ModelProp
    fun setTagThread(thread: UiTagThread) {
        binding.questionTextView.text = thread.question
        binding.dateTextView.text = prettyTime.format(thread.createdAt.toJavaInstant())
        binding.imageView.isVisible = !thread.imageUrl.isNullOrEmpty()
        binding.imageMarginEndSpace.isVisible = !thread.imageUrl.isNullOrEmpty()
        if (!thread.imageUrl.isNullOrEmpty()) {
            binding.imageView.load(context, thread.imageUrl)
        }
    }
}
