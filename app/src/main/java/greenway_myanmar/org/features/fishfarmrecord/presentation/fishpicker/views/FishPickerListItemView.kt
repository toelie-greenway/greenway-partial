package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FishPickerListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.FishPickerListItemUiState
import greenway_myanmar.org.util.extensions.load

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT, fullSpan = false)
class FishPickerListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: FishPickerListItemViewBinding =
        FishPickerListItemViewBinding.inflate(
            LayoutInflater.from(context),
            this
        )

    var item: FishPickerListItemUiState? = null
        @ModelProp set(item) {
            if (field != item) {
                if (item != null) {
                    binding.text.text = item.fish.name
                    binding.icon.load(context, item.fish.imageUrl)
                    refreshColor(item.checked)
                }
                field = item
            }
        }

    var onItemClickCallback: OnClickListener? = null
        @CallbackProp set

    init {
        orientation = VERTICAL
        binding.root.setOnClickListener {
            onItemClickCallback?.onClick(it)
        }
    }

    private fun refreshColor(checked: Boolean) {
        if (checked) {
            binding.checkIcon.visibility = VISIBLE
            binding.checkFrame.visibility = VISIBLE
            binding.text.setTextColor(ContextCompat.getColor(context, R.color.app_primary_text))
        } else {
            binding.checkIcon.visibility = GONE
            binding.checkFrame.visibility = GONE
            binding.text.setTextColor(ContextCompat.getColor(context, R.color.app_secondary_text))
        }
    }
}