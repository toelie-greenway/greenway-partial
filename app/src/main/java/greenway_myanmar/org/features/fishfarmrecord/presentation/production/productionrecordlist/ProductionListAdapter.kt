package greenway_myanmar.org.features.fishfarmrecord.presentation.production.productionrecordlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrProductionListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiProductionRecord

class ProductionListAdapter :
    ListAdapter<UiProductionRecord, ProductionListItemViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductionListItemViewHolder {
        return ProductionListItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ProductionListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object DiffItemCallback : ItemCallback<UiProductionRecord>() {
        override fun areItemsTheSame(
            oldItem: UiProductionRecord,
            newItem: UiProductionRecord
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UiProductionRecord,
            newItem: UiProductionRecord
        ): Boolean {
            return oldItem == newItem
        }
    }
}

class ProductionListItemViewHolder(
    parent: ViewGroup
) : ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_production_list_item_view, parent, false)
) {
    private val context: Context = parent.context
    private val binding = FfrProductionListItemViewBinding.bind(itemView)

    fun bind(item: UiProductionRecord) {
        binding.itemView.bind(item)
//
//        binding.dateTextView.text = DateUtils.format(item.date, "d MMM yyyy")
//        binding.totalProductionTextView.text = context.resources.getString(
//            R.string.formatted_viss, numberFormat.format(item.totalWeight)
//        )
//        binding.noteTextView.text = item.note
//        binding.noteTextView.isVisible = item.note.isNotEmpty()
    }
}