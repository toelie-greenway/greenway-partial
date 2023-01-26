package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.farminput.products

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct

class FarmInputProductAdapter(
    private val itemClickCallback: ItemClickCallback
) : ListAdapter<UiFarmInputProduct, FarmInputProductPickerViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FarmInputProductPickerViewHolder {
        return FarmInputProductPickerViewHolder(
            parent = parent,
            onItemClicked = {
                itemClickCallback.onItemClick(it)
            }
        )
    }

    override fun onBindViewHolder(holder: FarmInputProductPickerViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    interface ItemClickCallback {
        fun onItemClick(item: UiFarmInputProduct)
    }

    object DiffItemCallback : DiffUtil.ItemCallback<UiFarmInputProduct>() {
        override fun areItemsTheSame(
            oldItem: UiFarmInputProduct,
            newItem: UiFarmInputProduct
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UiFarmInputProduct,
            newItem: UiFarmInputProduct
        ): Boolean {
            return oldItem == newItem
        }
    }
}
