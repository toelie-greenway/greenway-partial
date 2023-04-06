package com.greenwaymyanmar.common.feature.category.data.repository

import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import com.greenwaymyanmar.common.data.api.v3.getDataOrThrow
import com.greenwaymyanmar.common.feature.category.data.source.network.model.NetworkCategory
import com.greenwaymyanmar.common.feature.category.data.source.network.model.asDomainModel
import com.greenwaymyanmar.common.feature.category.domain.model.Category
import com.greenwaymyanmar.common.feature.category.domain.model.CategoryType
import com.greenwaymyanmar.common.feature.category.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultCategoryRepository @Inject constructor(
    private val network: GreenWayWebservice
) : CategoryRepository {

    private val cachedCategoriesMutex = Mutex()
    private var cachedCategories: List<Category> = listOf()

    override fun getCategoriesStream(
        categoryType: CategoryType,
        forceRefresh: Boolean
    ): Flow<List<Category>> {
        return flow {
            val categories = cachedCategories
            if (forceRefresh || categories.isEmpty()) {
                val networkResult =
                    network.getCategories()
                        .getDataOrThrow().map(NetworkCategory::asDomainModel)
                cachedCategoriesMutex.withLock {
                    cachedCategories = networkResult
                }
            }
            emit(
                cachedCategoriesMutex.withLock {
                    Timber.d("Parent: ${categoryType.categoryId().id}")
                    cachedCategories.filter {
                        Timber.d("Category: $it")
                        it.parentCategoryId == categoryType.categoryId().id.toString()
                    }.map {
                        Timber.d("Filtered: $it")
                        it
                    }
                }
            )
        }
    }
}