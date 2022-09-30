package greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FarmingRecordQrFarmActivityListItemBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.QrFarmActivityAdapter.QrFarmActivityViewHolder.PhysicalPosition.*

class QrFarmActivityAdapter :
    ListAdapter<UiFarmActivity, QrFarmActivityAdapter.QrFarmActivityViewHolder>(
        QrFarmActivityDiffCallback
    ) {

    class QrFarmActivityViewHolder(
        val binding: FarmingRecordQrFarmActivityListItemBinding,
        val position: PhysicalPosition
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val topRouteView: View = binding.topRoute
        private val bottomRouteView: View = binding.bottomRoute
        private val activityNameTextView: TextView = binding.activityNameTextView
        private val dateTextView: TextView = binding.dateTextView
        private val detailTextView: TextView = binding.detailTextView

        init {
            when (position) {
                Normal -> {
                    topRouteView.isVisible = true
                    bottomRouteView.isVisible = true
                }
                First -> {
                    topRouteView.isVisible = false
                    bottomRouteView.isVisible = true
                }
                Last -> {
                    topRouteView.isVisible = true
                    bottomRouteView.isVisible = false
                }
            }
        }

        fun bind(item: UiFarmActivity) {
            activityNameTextView.text = item.activityName
            dateTextView.text = item.formattedDate

            detailTextView.text = item.formattedFarmInputs()
            detailTextView.isVisible = item.farmInputs.isNotEmpty()
        }

        enum class PhysicalPosition {
            First, Last, Normal;

            companion object {
                fun fromItemViewType(viewType: Int): PhysicalPosition {
                    return when (ItemViewType.fromValue(viewType)) {
                        ItemViewType.Normal -> Normal
                        ItemViewType.First -> First
                        ItemViewType.Last -> Last
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QrFarmActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.farming_record_qr_farm_activity_list_item, parent, false)
        return QrFarmActivityViewHolder(
            FarmingRecordQrFarmActivityListItemBinding.bind(view),
            QrFarmActivityViewHolder.PhysicalPosition.fromItemViewType(viewType)
        )
    }

    override fun onBindViewHolder(holder: QrFarmActivityViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return ItemViewType.fromPosition(position, itemCount).value
    }

    enum class ItemViewType(val value: Int) {
        First(0),
        Normal(1),
        Last(2);

        companion object {
            fun fromPosition(position: Int, itemCount: Int): ItemViewType {
                return when (position) {
                    0 -> First
                    itemCount - 1 -> Last
                    else -> Normal
                }
            }

            fun fromValue(value: Int): ItemViewType {
                return when (value) {
                    Normal.value -> Normal
                    First.value -> First
                    Last.value -> Last
                    else -> Normal
                }
            }
        }
    }

    object QrFarmActivityDiffCallback : DiffUtil.ItemCallback<UiFarmActivity>() {
        override fun areItemsTheSame(
            oldItem: UiFarmActivity,
            newItem: UiFarmActivity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: UiFarmActivity,
            newItem: UiFarmActivity
        ): Boolean {
            return oldItem == newItem
        }

    }
}
