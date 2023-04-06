package com.greenwaymyanmar.common.feature.category.domain.repository

import com.greenwaymyanmar.common.feature.category.domain.model.Category
import com.greenwaymyanmar.common.feature.category.domain.model.CategoryType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategoriesStream(
        categoryType: CategoryType,
        forceRefresh: Boolean = false
    ): Flow<List<Category>>
}