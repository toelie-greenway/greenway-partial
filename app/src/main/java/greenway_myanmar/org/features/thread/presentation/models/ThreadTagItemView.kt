package greenway_myanmar.org.features.thread.presentation.models

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.ThreadTagItemViewBinding
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.kotlin.customViewMergeBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ThreadTagItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding = customViewMergeBinding(ThreadTagItemViewBinding::inflate)

    var tag: UiTag? = null
        @ModelProp set(value) {
            if (value != field) {
                field = value
            }
        }

    var index: Int? = null
        @ModelProp set(value) {
            if (value != field) {
                field = value
            }
        }

    init {
        orientation = VERTICAL
        setPadding(0, 0, 0, 8.dp(context).toInt())
    }

    @AfterPropsSet
    fun bind() {
        if (tag == null) return

        binding.titleTextView.text =
            resources.getString(R.string.tag_item_formatted_title, index?.plus(1) ?: 1)
        binding.tagNameTextView.text = tag?.name.orEmpty()
        binding.categoryNameTextView.text = tag?.category?.name.orEmpty()
        binding.imageView.load(
            context,
            tag?.imageUrls?.firstOrNull()
        )

        // binding.tagColorIndicator.setCircleColor(tag?.color ?: Color.BLACK)
    }
}