package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ClosedSeasonAdapter(
    private val onItemClicked: (ClosedSeasonListItemUiState) -> Unit
) : ListAdapter<ClosedSeasonListItemUiState, ClosedSeasonListItemViewHolder>(
    ClosedSeasonDiffCallback
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClosedSeasonListItemViewHolder {
        return ClosedSeasonListItemViewHolder(
            parent = parent,
            onItemClick = {
                onItemClicked(it)
            }
        )
    }

    override fun onBindViewHolder(holder: ClosedSeasonListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object ClosedSeasonDiffCallback : DiffUtil.ItemCallback<ClosedSeasonListItemUiState>() {
        override fun areItemsTheSame(
            oldItem: ClosedSeasonListItemUiState,
            newItem: ClosedSeasonListItemUiState
        ): Boolean {
            return oldItem.seasonId == newItem.seasonId
        }

        override fun areContentsTheSame(
            oldItem: ClosedSeasonListItemUiState,
            newItem: ClosedSeasonListItemUiState
        ): Boolean {
            return oldItem == newItem
        }
    }

}
