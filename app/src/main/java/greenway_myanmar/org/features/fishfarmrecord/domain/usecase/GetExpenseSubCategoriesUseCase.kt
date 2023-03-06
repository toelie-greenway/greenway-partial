package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseSubCategoriesUseCase @Inject constructor(
    private val repository: ExpenseCategoryRepository
) {

    operator fun invoke(request: GetExpenseSubCategoriesRequest): Flow<List<ExpenseCategory>> {
        return repository.getExpenseSubCategoriesStream(
            categoryId = request.categoryId
        )
    }

    data class GetExpenseSubCategoriesRequest(
        val categoryId: String
    )

}