package greenway_myanmar.org.features.tag.presentation.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.TagProductSubheaderViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagProductSubheaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding =
        TagProductSubheaderViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
        setPadding(0, resources.getDimensionPixelSize(R.dimen.spacing_16), 0, resources.getDimensionPixelSize(R.dimen.spacing_8))
    }
}
