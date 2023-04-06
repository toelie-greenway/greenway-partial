package com.greenwaymyanmar.common.feature.category.domain.usecase

import com.greenwaymyanmar.common.feature.category.domain.model.Category
import com.greenwaymyanmar.common.feature.category.domain.model.CategoryType
import com.greenwaymyanmar.common.feature.category.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesStreamUseCase
@Inject
constructor(private val repository: CategoryRepository) {

    operator fun invoke(request: GetCategoriesRequest): Flow<List<Category>> {
        return repository.getCategoriesStream(request.categoryType)
    }

    data class GetCategoriesRequest(
        val categoryType: CategoryType
    )
}
