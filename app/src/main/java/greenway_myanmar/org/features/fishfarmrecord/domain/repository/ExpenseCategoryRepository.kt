package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {
    fun getExpensesByCategoryStream(): Flow<List<CategoryExpense>>
    fun getExpenseCategoriesStream(seasonId: String): Flow<List<ExpenseCategory>>
}