package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.CategoryExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryExpensesUseCase @Inject constructor(
    private val repository: CategoryExpenseRepository
) {
    suspend operator fun invoke(request: GetExpenseSummaryRequest): Flow<Result<List<CategoryExpense>>> {
        return repository.getCategoryExpensesStream(request.seasonId)
    }

    data class GetExpenseSummaryRequest(
        val seasonId: String
    )
}