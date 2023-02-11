package greenway_myanmar.org.features.fishfarmrecord.presentation.season.addeditseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFishInputItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

class FishInputItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var _fish: UiFish? = null
    private var onFishRemoved: (UiFish) -> Unit = {}

    private val binding = FfrFishInputItemViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        binding.removeButton.setOnClickListener {
            _fish?.let {
                onFishRemoved(it)
            }
        }
    }

    fun init(
        onFishRemoved: (UiFish) -> Unit
    ) {
        this.onFishRemoved = onFishRemoved
    }

    fun setFish(fish: UiFish) {
        if (_fish == fish) {
            return
        }

        _fish = fish
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