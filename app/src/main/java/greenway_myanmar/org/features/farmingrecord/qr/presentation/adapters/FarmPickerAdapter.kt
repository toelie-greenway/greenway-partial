package greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import greenway_myanmar.org.R
import greenway_myanmar.org.common.presentation.adapter.LoadStatePagedListAdapter
import greenway_myanmar.org.databinding.FarmPickerListItemBinding
import greenway_myanmar.org.databinding.NetworkStateItemBinding
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.ui.common.DataBoundViewHolder
import greenway_myanmar.org.ui.common.RetryCallback
import greenway_myanmar.org.vo.NetworkState

class FarmPickerAdapter(
    private val itemClickCallback: ItemClickCallback?,
    private val retryCallback: RetryCallback
) : LoadStatePagedListAdapter<Farm, DataBoundViewHolder<ViewDataBinding>>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<ViewDataBinding> {
        val binding: ViewDataBinding
        if (viewType == R.layout.farm_picker_list_item) {
            binding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.farm_picker_list_item,
                    parent,
                    false
                )
            setupItemBinding(binding as FarmPickerListItemBinding)
            return DataBoundViewHolder(binding)
        } else if (viewType == R.layout.network_state_item) {
            binding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.network_state_item,
                    parent,
                    false
                )
            return DataBoundViewHolder(binding)
        }
        throw IllegalArgumentException("unknown view type \$viewType")
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ViewDataBinding>, position: Int) {
        if (holder.binding is FarmPickerListItemBinding) {
            val item = getItem(position)
            holder.binding.farm = if (item == null) null else UiFarm.fromDomain(item)
        } else if (holder.binding is NetworkStateItemBinding) {
            holder.binding.networkState = getNetworkState()
            holder.binding.retryCallback = retryCallback
        }
        holder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.farm_picker_list_item
        }
    }

    private fun setupItemBinding(binding: FarmPickerListItemBinding) {
        binding.itemClickCallback = itemClickCallback
    }

    interface ItemClickCallback {
        fun onItemClick(item: UiFarm)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Farm> =
            object : DiffUtil.ItemCallback<Farm>() {
                override fun areItemsTheSame(oldItem: Farm, newItem: Farm): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Farm, newItem: Farm): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
