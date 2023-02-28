package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {
    fun getExpenseCategoriesStream(forceRefresh: Boolean = false): Flow<List<ExpenseCategory>>
}