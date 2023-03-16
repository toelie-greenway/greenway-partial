package greenway_myanmar.org.features.fishfarmrecord.presentation.season.fishes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonFishListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.extensions.load

class SeasonFishListViewHolder constructor(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_season_fish_list_item_view, parent, false)
) {
    private val context: Context = parent.context
    private val binding = FfrSeasonFishListItemViewBinding.bind(itemView)

    fun bind(item: UiFish) {
        binding.fishName.text = item.name
        binding.fishSpecies.text = item.species
        binding.fishSpecies.isVisible = item.species.isNotEmpty()
        bindImage(item.imageUrl)
    }

    private fun bindImage(imageUrl: String) {
        binding.fishIcon.load(context, imageUrl)
    }
}