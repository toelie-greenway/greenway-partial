package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter

class FcrRecordListAdapter : ListAdapter<FcrRecordListItemUiState, FcrRecordListItemViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FcrRecordListItemViewHolder {
        return FcrRecordListItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: FcrRecordListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object DiffItemCallback: ItemCallback<FcrRecordListItemUiState>() {
        override fun areItemsTheSame(
            oldItem: FcrRecordListItemUiState,
            newItem: FcrRecordListItemUiState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FcrRecordListItemUiState,
            newItem: FcrRecordListItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }
}