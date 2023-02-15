package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveExpenseUseCase.SaveExpenseRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveExpenseUseCase.SaveExpenseResult

interface ExpenseRepository {
    suspend fun saveExpense(request: SaveExpenseRequest): SaveExpenseResult
}