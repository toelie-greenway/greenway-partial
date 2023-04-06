package com.greenwaymyanmar.common.feature.category.presentation.categorypicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.CategoryPickerListItemBinding

class CategoryPickerViewHolder(
  parent: ViewGroup,
  onItemClicked: (CategoryPickerListItemUiState) -> Unit
) :
  ViewHolder(
    LayoutInflater.from(parent.context)
      .inflate(R.layout.category_picker_list_item, parent, false)
  ) {
  private val binding = CategoryPickerListItemBinding.bind(itemView)
  private var _item: CategoryPickerListItemUiState? = null

  init {
    binding.root.setOnClickListener { onItemClicked(onItemClicked) }
  }

  private fun onItemClicked(onItemClicked: (CategoryPickerListItemUiState) -> Unit) {
    _item?.let { item -> onItemClicked(item) }
  }

  fun bind(item: CategoryPickerListItemUiState) {
    _item = item
    binding.categoryNameTextView.text = item.category.name
    binding.root.isChecked = item.checked
  }
}
