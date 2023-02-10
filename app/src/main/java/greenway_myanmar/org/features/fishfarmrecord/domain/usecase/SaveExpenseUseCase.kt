package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseRepository
import kotlinx.datetime.Instant
import java.math.BigDecimal
import javax.inject.Inject

class SaveExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(request: SaveExpenseRequest): SaveExpenseResult {
        return repository.saveExpense(request)
    }

    data class SaveExpenseRequest(
        val seasonId: String,
        val expenseCategory: ExpenseCategory,
        val date: Instant,
        val labourQuantity: Int? = null,
        val labourCost: BigDecimal? = null,
        val familyQuantity: Int? = null,
        val familyCost: BigDecimal? = null,
        val machineryCost: BigDecimal? = null,
        val images: List<String>? = null,
        val remark: String? = null,
        val inputs: List<FarmInputCost>? = null
    )

    data class SaveExpenseResult(
        val id: String
    )
}