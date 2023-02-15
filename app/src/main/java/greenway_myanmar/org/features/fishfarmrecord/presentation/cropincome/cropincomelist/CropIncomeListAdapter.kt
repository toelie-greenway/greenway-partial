package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.cropincomelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrCropIncomeListItemViewBinding
import greenway_myanmar.org.util.DateUtils

class CropIncomeListAdapter : ListAdapter<CropIncomeListUiState, CropIncomeListItemViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropIncomeListItemViewHolder {
        return CropIncomeListItemViewHolder(
            parent = parent
        )
    }

    override fun onBindViewHolder(holder: CropIncomeListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object DiffItemCallback : ItemCallback<CropIncomeListUiState>() {
        override fun areItemsTheSame(
            oldItem: CropIncomeListUiState,
            newItem: CropIncomeListUiState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CropIncomeListUiState,
            newItem: CropIncomeListUiState
        ): Boolean {
            return oldItem == newItem
        }

    }
}

class CropIncomeListItemViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_crop_income_list_item_view, parent, false)
) {
    private val binding = FfrCropIncomeListItemViewBinding.bind(itemView)

    fun bind(item: CropIncomeListUiState) {
        binding.dateTextView.text = DateUtils.format(item.date, "d MMM yyyy")
        binding.incomeTextView.setAmount(item.income)
        binding.cropNameTextView.text = item.cropName
    }
}