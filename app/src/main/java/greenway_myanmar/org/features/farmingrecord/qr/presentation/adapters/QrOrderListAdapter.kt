package greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.databinding.FarmingRecordQrOrderListItemBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.QrOrderStatusItemUiState
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrOrder

class QrOrderListAdapter constructor(
    private val itemClickCallback: ItemClickCallback
) :
    ListAdapter<UiQrOrder, QrOrderListAdapter.QrOrderViewHolder>(QrOrderDiffCallback) {

    class QrOrderViewHolder(
        private val binding: FarmingRecordQrOrderListItemBinding,
        private val itemClickCallback: ItemClickCallback
    ) : RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(item: UiQrOrder) {
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
        return QrOrderViewHolder(binding, itemClickCallback)
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