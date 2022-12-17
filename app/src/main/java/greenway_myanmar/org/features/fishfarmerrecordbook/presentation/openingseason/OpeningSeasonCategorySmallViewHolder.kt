package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.view.ViewGroup
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrOpeningSeasonCategorySmallListItemBinding

class OpeningSeasonCategorySmallViewHolder(
    val parent: ViewGroup
) : OpeningSeasonCategoryViewHolder(parent, R.layout.ffr_opening_season_category_small_list_item) {

    val binding = FfrOpeningSeasonCategorySmallListItemBinding.bind(itemView)

    override fun bind(item: OpeningSeasonCategoryListItemUiState) {
        binding.categoryName.text = item.categoryName
    }
}