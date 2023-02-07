package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategoryWithTotalExpenses
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {
    fun getExpenseCategoriesWithTotalExpensesStream(): Flow<List<ExpenseCategoryWithTotalExpenses>>
    fun getExpenseCategoriesStream(): Flow<List<ExpenseCategory>>
}