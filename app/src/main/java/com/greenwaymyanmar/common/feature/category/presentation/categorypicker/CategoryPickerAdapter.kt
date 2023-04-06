package com.greenwaymyanmar.common.feature.category.presentation.categorypicker

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory

class CategoryPickerAdapter(private val itemClickCallback: ItemClickCallback) :
  ListAdapter<CategoryPickerListItemUiState, CategoryPickerViewHolder>(
    DiffItemCallback
  ) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CategoryPickerViewHolder {
    return CategoryPickerViewHolder(
      parent = parent,
      onItemClicked = { itemClickCallback.onItemClicked(it.category) }
    )
  }

  override fun onBindViewHolder(holder: CategoryPickerViewHolder, position: Int) {
    val item = getItem(position)
    if (item != null) {
      holder.bind(item)
    }
  }

  interface ItemClickCallback {
    fun onItemClicked(category: UiCategory)
  }

  object DiffItemCallback : DiffUtil.ItemCallback<CategoryPickerListItemUiState>() {
    override fun areItemsTheSame(
      oldItem: CategoryPickerListItemUiState,
      newItem: CategoryPickerListItemUiState
    ): Boolean {
      return oldItem.category.id == newItem.category.id
    }

    override fun areContentsTheSame(
      oldItem: CategoryPickerListItemUiState,
      newItem: CategoryPickerListItemUiState
    ): Boolean {
      return oldItem == newItem
    }
  }
}
