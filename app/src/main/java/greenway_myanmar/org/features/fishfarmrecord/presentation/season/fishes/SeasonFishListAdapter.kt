package greenway_myanmar.org.features.fishfarmrecord.presentation.season.fishes

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

class SeasonFishListAdapter : ListAdapter<UiFish, SeasonFishListViewHolder>(DiffItemUtilCallback) {

    object DiffItemUtilCallback : DiffUtil.ItemCallback<UiFish>() {
        override fun areItemsTheSame(oldItem: UiFish, newItem: UiFish): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UiFish, newItem: UiFish): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonFishListViewHolder {
        return SeasonFishListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SeasonFishListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}