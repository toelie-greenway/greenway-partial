package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategoryWithTotalExpenses
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {
    fun getExpenseCategoriesWithTotalExpensesStream(): Flow<Result<List<ExpenseCategoryWithTotalExpenses>>>
    fun getExpenseCategoriesStream(): Flow<List<ExpenseCategory>>
}