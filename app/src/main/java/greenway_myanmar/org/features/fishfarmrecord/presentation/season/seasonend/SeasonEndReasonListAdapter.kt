package greenway_myanmar.org.features.fishfarmrecord.presentation.season.seasonend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonEndReasonListItemViewBinding
import timber.log.Timber

class SeasonEndReasonListAdapter(
    private val onItemClick: (item: SeasonEndReasonListItemUiState) -> Unit,
) : ListAdapter<SeasonEndReasonListItemUiState, SeasonEndReasonListItemViewHolder>(ItemDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeasonEndReasonListItemViewHolder {
        return SeasonEndReasonListItemViewHolder(
            parent = parent,
            onItemClick = onItemClick,
        )
    }

    override fun onBindViewHolder(holder: SeasonEndReasonListItemViewHolder, position: Int) {
        val item = getItem(position)
        Timber.d("Bind: $item")
        if (item != null) {
            holder.bind(item)
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<SeasonEndReasonListItemUiState>() {
        override fun areItemsTheSame(
            oldItem: SeasonEndReasonListItemUiState,
            newItem: SeasonEndReasonListItemUiState
        ): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areContentsTheSame(
            oldItem: SeasonEndReasonListItemUiState,
            newItem: SeasonEndReasonListItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }
}


class SeasonEndReasonListItemViewHolder(
    private val parent: ViewGroup,
    private val onItemClick: (item: SeasonEndReasonListItemUiState) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_season_end_reason_list_item_view, parent, false)
) {
    private val binding = FfrSeasonEndReasonListItemViewBinding.bind(itemView)

    fun bind(item: SeasonEndReasonListItemUiState) {
        binding.reasonTextView.text = item.item.name
        binding.root.isChecked = item.checked
        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}