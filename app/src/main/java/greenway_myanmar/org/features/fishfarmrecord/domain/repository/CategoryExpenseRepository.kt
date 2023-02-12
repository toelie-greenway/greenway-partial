package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import kotlinx.coroutines.flow.Flow

interface CategoryExpenseRepository {
    suspend fun getCategoryExpensesStream(
        seasonId: String,
        forceRefresh: Boolean = false
    ): Flow<Result<List<CategoryExpense>>>

    fun getCategoryExpenseStream(
        seasonId: String,
        categoryId: String,
        forceRefresh: Boolean = false
    ): Flow<Result<CategoryExpense?>>
}