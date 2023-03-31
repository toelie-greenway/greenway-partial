package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagPost
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.TagPostItemViewBinding
import greenway_myanmar.org.util.extensions.load
import kotlinx.datetime.toJavaInstant
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

private val prettyTime = PrettyTime(Date(), Locale("MM"))

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagPostItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = TagPostItemViewBinding.inflate(LayoutInflater.from(context), this, true)

    @ModelProp
    fun setTagPost(post: UiTagPost) {
        binding.postTitleTextView.text = post.title
        bindPostInfo(post)
        bindImage(post.imageUrl)
    }

    private fun bindPostInfo(post: UiTagPost) {
        val date = prettyTime.format(post.createdAt.toJavaInstant())
        binding.postInfoTextView.text =
            resources.getString(R.string.tag_post_formatted_info, date, post.author)
    }

    private fun bindImage(imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            binding.imageView.load(context, imageUrl)
            binding.imageView.isVisible = true
            binding.imageMarginEndSpace.isVisible = true
        } else {
            binding.imageView.isVisible = false
            binding.imageMarginEndSpace.isVisible = false
        }
    }
}
