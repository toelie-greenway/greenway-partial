package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R

class FcrRecordListItemViewHolder (val parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_fcr_record_list_item_view, parent, false)
) {

    fun bind(item: FcrRecordListItemUiState) {

    }
}