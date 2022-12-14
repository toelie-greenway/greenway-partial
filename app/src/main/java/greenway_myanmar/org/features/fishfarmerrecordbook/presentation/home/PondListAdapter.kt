package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.ContractFarmingCompany

class PondListAdapter(
    private val onItemClick: (item: PondListItemUiState) -> Unit,
    private val onCompanyClick: (company: ContractFarmingCompany) -> Unit,
    private val onAddNewSeasonClick: (item: PondListItemUiState) -> Unit,
    private val onAddNewExpenseClick: (item: PondListItemUiState) -> Unit
) : ListAdapter<PondListItemUiState, PondListItemViewHolder>(PondDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PondListItemViewHolder {
        return PondListItemViewHolder(
            parent = parent,
            onItemClick = onItemClick,
            onCompanyClick = onCompanyClick,
            onAddNewSeasonClick = onAddNewSeasonClick,
            onAddNewExpenseClick = onAddNewExpenseClick
        )
    }

    override fun onBindViewHolder(holder: PondListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object PondDiffCallback : DiffUtil.ItemCallback<PondListItemUiState>() {
        override fun areItemsTheSame(
            oldItem: PondListItemUiState,
            newItem: PondListItemUiState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PondListItemUiState,
            newItem: PondListItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }
}