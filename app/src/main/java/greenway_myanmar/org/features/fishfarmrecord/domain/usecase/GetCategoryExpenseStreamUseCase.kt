package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.CategoryExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryExpenseStreamUseCase @Inject constructor(
    private val repository: CategoryExpenseRepository
) {
    operator fun invoke(request: GetExpenseRequest): Flow<Result<CategoryExpense?>> {
        return repository.getCategoryExpenseStream(request.seasonId, request.categoryId)
    }

    data class GetExpenseRequest(
        val seasonId: String,
        val categoryId: String
    )
}