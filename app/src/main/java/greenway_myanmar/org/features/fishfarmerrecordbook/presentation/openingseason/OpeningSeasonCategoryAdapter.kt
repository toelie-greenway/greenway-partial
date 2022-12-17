package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

private const val ITEM_VIEW_TYPE_SMALL = 0
private const val ITEM_VIEW_TYPE_LARGE = 1

class OpeningSeasonCategoryAdapter :
    ListAdapter<OpeningSeasonCategoryListItemUiState, OpeningSeasonCategoryViewHolder>(
        CategoryDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpeningSeasonCategoryViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_LARGE) {
            OpeningSeasonCategoryLargeViewHolder(parent)
        } else {
            OpeningSeasonCategorySmallViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: OpeningSeasonCategoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.hasRecord) {
            ITEM_VIEW_TYPE_LARGE
        } else {
            ITEM_VIEW_TYPE_SMALL
        }
    }

    object CategoryDiffCallback : DiffUtil.ItemCallback<OpeningSeasonCategoryListItemUiState>() {

        override fun areItemsTheSame(
            oldItem: OpeningSeasonCategoryListItemUiState,
            newItem: OpeningSeasonCategoryListItemUiState
        ): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(
            oldItem: OpeningSeasonCategoryListItemUiState,
            newItem: OpeningSeasonCategoryListItemUiState
        ): Boolean {
            return oldItem == newItem
        }
    }
}