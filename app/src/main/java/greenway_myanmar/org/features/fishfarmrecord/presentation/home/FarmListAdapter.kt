package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany

class FarmListAdapter(
    private val onItemClick: (item: FarmListItemUiState) -> Unit,
    private val onCompanyClick: (company: ContractFarmingCompany) -> Unit,
    private val onAddNewSeasonClick: (item: FarmListItemUiState) -> Unit,
    private val onAddNewExpenseClick: (item: FarmListItemUiState) -> Unit
) : ListAdapter<FarmListItemUiState, FarmListItemViewHolder>(PondDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmListItemViewHolder {
        return FarmListItemViewHolder(
            parent = parent,
            onItemClick = onItemClick,
            onCompanyClick = onCompanyClick,
            onAddNewSeasonClick = onAddNewSeasonClick,
            onAddNewExpenseClick = onAddNewExpenseClick
        )
    }

    override fun onBindViewHolder(holder: FarmListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object PondDiffCallback : DiffUtil.ItemCallback<FarmListItemUiState>() {
        override fun areItemsTheSame(
            oldItem: FarmListItemUiState,
            newItem: FarmListItemUiState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FarmListItemUiState,
            newItem: FarmListItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }
}