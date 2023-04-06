package com.greenwaymyanmar.common.feature.category.presentation.categorypicker

sealed class CategoryPickerEvent {
  object RetryLoadingCategories : CategoryPickerEvent()
  data class ToggleCategorySelection(val categoryId: String) : CategoryPickerEvent()
}
