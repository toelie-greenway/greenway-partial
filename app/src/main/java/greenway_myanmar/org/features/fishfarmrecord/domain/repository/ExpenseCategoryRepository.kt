package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseByCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {
    fun getExpenseCategoriesWithTotalExpensesStream(): Flow<List<ExpenseByCategory>>
    fun getExpenseCategoriesStream(seasonId: String): Flow<List<ExpenseCategory>>
}