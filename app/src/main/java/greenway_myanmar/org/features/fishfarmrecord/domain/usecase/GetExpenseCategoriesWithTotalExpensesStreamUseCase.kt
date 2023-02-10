package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseByCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseCategoriesWithTotalExpensesStreamUseCase @Inject constructor(
    private val expenseCategoryRepository: ExpenseCategoryRepository
) {
    operator fun invoke(): Flow<List<ExpenseByCategory>> {
        return expenseCategoryRepository.getExpenseCategoriesWithTotalExpensesStream()
    }
}