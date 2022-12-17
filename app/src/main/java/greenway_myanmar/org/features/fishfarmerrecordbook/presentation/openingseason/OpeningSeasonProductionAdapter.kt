package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Production

private const val ITEM_VIEW_TYPE_SMALL = 2
private const val ITEM_VIEW_TYPE_LARGE = 3

class OpeningSeasonProductionAdapter : RecyclerView.Adapter<OpeningSeasonProductionViewHolder>() {
//    ListAdapter<OpeningSeasonProductionListItemUiState, OpeningSeasonProductionViewHolder>(
//        ProductionDiffCallback
//    ) {

    var uiState: OpeningSeasonProductionListItemUiState? = null
        set(uiState) {
            if (field != uiState) {
                val displayOldItem = displayAsItem(field)
                val displayNewItem = displayAsItem(uiState)

                if (displayOldItem && !displayNewItem) {
                    notifyItemRemoved(0)
                } else if (displayNewItem && !displayOldItem) {
                    notifyItemInserted(0)
                } else if (displayOldItem && displayNewItem) {
                    notifyItemChanged(0)
                }

                field = uiState
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpeningSeasonProductionViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_LARGE) {
            OpeningSeasonProductionLargeViewHolder(parent)
        } else {
            OpeningSeasonProductionSmallViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: OpeningSeasonProductionViewHolder, position: Int) {
        val item = uiState
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (uiState?.hasRecord == true) {
            ITEM_VIEW_TYPE_LARGE
        } else {
            ITEM_VIEW_TYPE_SMALL
        }
    }

    object ProductionDiffCallback :
        DiffUtil.ItemCallback<OpeningSeasonProductionListItemUiState>() {

        override fun areItemsTheSame(
            oldItem: OpeningSeasonProductionListItemUiState,
            newItem: OpeningSeasonProductionListItemUiState
        ): Boolean {
            // Production List Item is always one
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: OpeningSeasonProductionListItemUiState,
            newItem: OpeningSeasonProductionListItemUiState
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemCount() = if (displayAsItem(uiState)) 1 else 0

    private fun displayAsItem(uiState: OpeningSeasonProductionListItemUiState?): Boolean {
        return uiState != null
    }
}