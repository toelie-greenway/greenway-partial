package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.view.ViewGroup
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrOpeningSeasonCategorySmallListItemBinding
import greenway_myanmar.org.databinding.FfrOpeningSeasonProductionSmallListItemBinding

class OpeningSeasonProductionSmallViewHolder(
    val parent: ViewGroup
) : OpeningSeasonProductionViewHolder(parent, R.layout.ffr_opening_season_production_small_list_item) {

    val binding = FfrOpeningSeasonProductionSmallListItemBinding.bind(itemView)

    override fun bind(item: OpeningSeasonProductionListItemUiState) {
        //binding.categoryName.text = item.categoryName
    }
}