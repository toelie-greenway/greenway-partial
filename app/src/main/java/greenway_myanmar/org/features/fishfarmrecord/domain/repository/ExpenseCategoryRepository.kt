package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategoryWithTotalExpenses
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {
    fun observeExpenseCategoriesWithTotalExpenses(): Flow<Result<List<ExpenseCategoryWithTotalExpenses>>>
}