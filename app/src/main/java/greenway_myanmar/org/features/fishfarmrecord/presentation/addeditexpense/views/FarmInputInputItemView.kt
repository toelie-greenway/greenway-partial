package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFarmInputInputItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost

class FarmInputInputItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var _item: UiFarmInputCost? = null

    private val binding = FfrFarmInputInputItemViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun bind(item: UiFarmInputCost) {
        if (_item == item) {
            return
        }

//        binding.fishName.text = fish.name
//        binding.fishSpecies.text = fish.species
//        binding.fishSpecies.isVisible = fish.species.isNotEmpty()
//        bindImage(fish.imageUrl)
    }

    private fun bindImage(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .animate(R.anim.image_fade_in)
            .fallback(R.drawable.image_placeholder_circle)
            .error(R.drawable.image_placeholder_circle)
            .placeholder(R.drawable.image_placeholder_circle)
            .into(binding.fishIcon)
    }
}