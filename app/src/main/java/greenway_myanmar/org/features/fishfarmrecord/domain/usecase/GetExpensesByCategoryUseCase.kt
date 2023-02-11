package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseByCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetExpensesByCategoryUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(request: GetExpensesByCategoryRequest): List<ExpenseByCategory> {
        return repository.getExpensesByCategory(request.seasonId)
    }

    data class GetExpensesByCategoryRequest(
        val seasonId: String
    )
}