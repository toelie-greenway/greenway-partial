package greenway_myanmar.org.features.fishfarmrecord.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.GreenWayFishInputItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

class GreenWayFishInputItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var _fish: UiFish? = null

    private val binding = GreenWayFishInputItemViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private fun init(context: Context) {

    }

    fun setFish(fish: UiFish) {
        if (_fish == fish) {
            return
        }

        binding.fishName.text = fish.name
        binding.fishSpecies.text = fish.species
        binding.fishSpecies.isVisible = fish.species.isNotEmpty()
        bindImage(fish.imageUrl)
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