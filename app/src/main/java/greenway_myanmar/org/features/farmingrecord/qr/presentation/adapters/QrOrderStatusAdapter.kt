package greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.LocaleListCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatus
import greenway_myanmar.org.features.farmingrecord.qr.presentation.QrOrderStatusItemUiState
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.FarmingRecordQrOrderStatusAdapter.OrderStatusViewHolder.PhysicalPosition
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

class FarmingRecordQrOrderStatusAdapter :
    ListAdapter<QrOrderStatusItemUiState, RecyclerView.ViewHolder>(
        QrOrderStatusDiffCallback
    ) {

    private var dimRoute: Boolean = false

    class OrderStatusPlaceholderViewHolder(val itemView: View) :
        RecyclerView.ViewHolder(itemView)

    class OrderStatusViewHolder(
        val itemView: View,
        val position: PhysicalPosition,
        val dimRoute: Boolean
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val topRouteView: View = itemView.findViewById(R.id.top_route)
        private val bottomRouteView: View = itemView.findViewById(R.id.bottom_route)
        private val statusTextView: TextView = itemView.findViewById(R.id.status_text_view)
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)
        private val noteTextView: TextView = itemView.findViewById(R.id.note_text_view)

        init {
            when (position) {
                PhysicalPosition.Normal -> {
                    topRouteView.isVisible = true
                    bottomRouteView.isVisible = true
                }
                PhysicalPosition.First -> {
                    topRouteView.isVisible = false
                    bottomRouteView.isVisible = true
                }
                PhysicalPosition.Last -> {
                    topRouteView.isVisible = true
                    bottomRouteView.isVisible = false
                }
            }
            bottomRouteView.alpha = if (dimRoute) 0.36f else 1f
        }

        fun bind(item: QrOrderStatus) {
            statusTextView.text = item.status
            dateTextView.text = dateFormatted(item.date)

            noteTextView.text = item.note.orEmpty()
            noteTextView.isVisible = !item.note.isNullOrEmpty()
        }

        private fun dateFormatted(date: Instant): String {
            val zoneId = ZoneId.systemDefault()
            val locale = Locale("my")// LocaleListCompat.getDefault().get(0)
            return DateTimeFormatter.ofPattern("d MMMM, yyyy၊ a h:mm")
                .withLocale(locale)
                .withDecimalStyle(DecimalStyle.of(locale))
                .withZone(zoneId)
                .format(date)
        }

        enum class PhysicalPosition {
            First, Last, Normal;

            companion object {
                fun fromItemViewType(viewType: Int): PhysicalPosition {
                    return when (ItemViewType.fromValue(viewType)) {
                        ItemViewType.Normal -> Normal
                        ItemViewType.First -> First
                        ItemViewType.Last -> Last
                        else -> {
                            throw IllegalStateException("Invalid view type: $viewType")
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == ItemViewType.Placeholder.value) {
            val view = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.farming_record_qr_order_status_placeholder_list_item,
                    parent,
                    false
                )
            return OrderStatusPlaceholderViewHolder(view)

        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.farming_record_qr_order_status_list_item, parent, false)
            return OrderStatusViewHolder(
                view,
                PhysicalPosition.fromItemViewType(viewType),
                dimRoute
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is QrOrderStatusItemUiState.ListItem -> {
                (holder as OrderStatusViewHolder).bind(item.item)
            }
            is QrOrderStatusItemUiState.PlaceholderItem -> {

            }
        }
    }

    override fun submitList(list: List<QrOrderStatusItemUiState>?) {
        dimRoute = list?.find { it == QrOrderStatusItemUiState.PlaceholderItem } != null
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is QrOrderStatusItemUiState.ListItem -> {
                ItemViewType.fromPosition(position, itemCount).value
            }
            is QrOrderStatusItemUiState.PlaceholderItem -> {
                ItemViewType.Placeholder.value
            }
        }
    }

    enum class ItemViewType(val value: Int) {
        First(0),
        Normal(1),
        Last(2),
        Placeholder(3);

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
                    Placeholder.value -> Placeholder
                    else -> Normal
                }
            }
        }
    }
}

object QrOrderStatusDiffCallback : DiffUtil.ItemCallback<QrOrderStatusItemUiState>() {
    override fun areItemsTheSame(
        oldItem: QrOrderStatusItemUiState,
        newItem: QrOrderStatusItemUiState
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: QrOrderStatusItemUiState,
        newItem: QrOrderStatusItemUiState
    ): Boolean {
        return oldItem == newItem
    }

}