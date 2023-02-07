package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategoryWithTotalExpenses
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseCategoriesWithTotalExpensesStreamUseCase @Inject constructor(
    private val expenseCategoryRepository: ExpenseCategoryRepository
) {
    operator fun invoke(): Flow<List<ExpenseCategoryWithTotalExpenses>> {
        return expenseCategoryRepository.getExpenseCategoriesWithTotalExpensesStream()
    }
}