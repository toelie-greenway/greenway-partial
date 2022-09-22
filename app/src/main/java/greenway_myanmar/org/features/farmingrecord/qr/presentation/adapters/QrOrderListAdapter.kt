package greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FarmingRecordQrOrderListItemBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrOrder

class QrOrderListAdapter constructor(
    private val context: Context,
    private val itemClickCallback: ItemClickCallback
) :
    ListAdapter<UiQrOrder, QrOrderListAdapter.QrOrderViewHolder>(QrOrderDiffCallback) {

    class QrOrderViewHolder(
        private val context: Context,
        private val binding: FarmingRecordQrOrderListItemBinding,
        private val itemClickCallback: ItemClickCallback
    ) : RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(item: UiQrOrder) {
            binding.farmInfoTextView.text =
                context.resources.getString(
                    R.string.label_formatted_qr_order_farm_info,
                    item.farmName,
                    item.cropName
                )
            binding.seasonInfoTextView.text =
                context.resources.getString(
                    R.string.label_formatted_qr_order_season_info,
                    item.seasonName,
                    item.seasonYear
                )
            binding.quantityTextView.text =
                context.resources.getString(
                    R.string.label_formatted_qr_order_quantity,
                    item.formattedQuantity
                )
            binding.orderStatusTextView.text =
                context.resources.getString(
                    R.string.label_formatted_qr_order_status,
                    item.orderStatusDate,
                    item.orderStatusDetail.statusLabel
                )
            binding.qrImageView.setQrData(item.qrUrl)
            binding.qrCardView.setOnClickListener {
                if (item.qrUrl.isNotEmpty()) {
                    itemClickCallback.onQrClick(item.qrUrl)
                }
            }
            binding.root.setOnClickListener {
                itemClickCallback.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QrOrderViewHolder {
        val binding = FarmingRecordQrOrderListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QrOrderViewHolder(context, binding, itemClickCallback)
    }

    override fun onBindViewHolder(holder: QrOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface ItemClickCallback {
        fun onQrClick(qrUrl: String)
        fun onItemClick(item: UiQrOrder)
    }
}

object QrOrderDiffCallback : DiffUtil.ItemCallback<UiQrOrder>() {
    override fun areItemsTheSame(
        oldItem: UiQrOrder,
        newItem: UiQrOrder
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UiQrOrder,
        newItem: UiQrOrder
    ): Boolean {
        return oldItem == newItem
    }

}