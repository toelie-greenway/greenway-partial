package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFcrRecordListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list.views.FcrRecordListFishItemView
import java.math.BigDecimal

class FcrRecordListItemViewHolder(
    parent: ViewGroup,
    private val onItemClicked: (FcrRecordListItemUiState) -> Unit,
    private val onEditClicked: (FcrRecordListItemUiState) -> Unit,
    private val onDeleteClicked: (FcrRecordListItemUiState) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_fcr_record_list_item_view, parent, false)
) {
    private val context = parent.context
    private val binding = FfrFcrRecordListItemViewBinding.bind(itemView)

    fun bind(item: FcrRecordListItemUiState) {
        binding.dateTextView.text = item.formattedDate()

        binding.listItemContainer.removeAllViews()
        item.ratios.forEachIndexed { index, fcr ->
            if (fcr.feedWeight > BigDecimal.ZERO || fcr.gainWeight > BigDecimal.ZERO) {
                binding.listItemContainer.addView(
                    FcrRecordListFishItemView(context).apply {
                        bind(fcr)
                    }
                )
            }
        }
    }
}