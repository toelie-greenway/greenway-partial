package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FishPickerListItemBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.FishPickerListItemUiState
import greenway_myanmar.org.ui.widget.GreenWayGridMediumItem

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT, fullSpan = false)
class FishPickerListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GreenWayGridMediumItem(context, attrs, defStyleAttr) {

    private val binding: FishPickerListItemBinding =
        FishPickerListItemBinding.inflate(
            LayoutInflater.from(context),
            this
        )

    var item: FishPickerListItemUiState? = null
        @ModelProp set(item) {
            if (field != item) {
                if (item != null) {
                    setLabel(item.fish.name)
                    setImageUrl(item.fish.imageUrl, R.drawable.image_placeholder_circle)
                    isChecked = item.checked
                }
                field = item
            }
        }

    var onItemClickCallback: OnClickListener? = null
        @CallbackProp set

    init {
        binding.root.setOnClickListener {
            onItemClickCallback?.onClick(it)
        }
    }
}