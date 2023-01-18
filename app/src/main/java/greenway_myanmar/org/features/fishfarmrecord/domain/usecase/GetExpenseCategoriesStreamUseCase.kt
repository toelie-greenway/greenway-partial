package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseCategoriesStreamUseCase @Inject constructor(
    private val repository: ExpenseCategoryRepository
) {

    operator fun invoke(): Flow<List<ExpenseCategory>> {
        return repository.getExpenseCategoriesStream()
    }
}