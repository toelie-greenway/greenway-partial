package greenway_myanmar.org.features.fishfarmerrecordbook.domain.usecase

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.domain.usecase.FlowUseCase
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.ExpenseCategoryWithTotalExpenses
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveExpenseCategoriesWithTotalExpensesUseCase @Inject constructor(
    private val expenseCategoryRepository: ExpenseCategoryRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<ExpenseCategoryWithTotalExpenses>>(ioDispatcher) {
    override fun execute(params: Unit): Flow<Result<List<ExpenseCategoryWithTotalExpenses>>> {
        return expenseCategoryRepository.observeExpenseCategoriesWithTotalExpenses()
    }
}