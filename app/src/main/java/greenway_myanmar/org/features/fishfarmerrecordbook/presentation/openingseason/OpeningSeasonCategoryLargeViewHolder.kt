package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.view.ViewGroup
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrOpeningSeasonCategoryLargeListItemBinding
import greenway_myanmar.org.databinding.FfrOpeningSeasonCategorySmallListItemBinding

class OpeningSeasonCategoryLargeViewHolder(
    val parent: ViewGroup
) : OpeningSeasonCategoryViewHolder(parent, R.layout.ffr_opening_season_category_large_list_item) {
    val binding = FfrOpeningSeasonCategoryLargeListItemBinding.bind(itemView)

    override fun bind(item: OpeningSeasonCategoryListItemUiState) {
        binding.categoryName.text = item.categoryName
    }
}