package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.farminput.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFarmInputProductPickerListItemBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.util.extensions.load

class FarmInputProductPickerViewHolder(
    parent: ViewGroup,
    private val onItemClicked: (UiFarmInputProduct) -> Unit
) : ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(
            R.layout.ffr_farm_input_product_picker_list_item, parent, false
        )
) {
    private val binding = FfrFarmInputProductPickerListItemBinding.bind(itemView)
    private val context = parent.context

    fun bind(item: UiFarmInputProduct) {
        binding.productNameTextView.text = item.name
        bindProductImage(item.thumbnail)
        binding.root.setOnClickListener { onItemClicked(item) }
    }

    private fun bindProductImage(thumbnail: String) {
        binding.productImageView.load(context, thumbnail)
    }
}