package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class UiCategoryExpense(
    val category: UiExpenseCategory,
    val totalExpenses: BigDecimal,
    val lastRecordDate: Instant? = null,
    val expenses: List<UiExpense> = emptyList()
) {
    companion object {
        fun fromDomainModel(domainModel: CategoryExpense) = UiCategoryExpense(
            category = UiExpenseCategory.fromDomain(domainModel.category),
            totalExpenses = domainModel.totalExpenses,
            lastRecordDate = domainModel.lastRecordDate,
            expenses = domainModel.expenses.map {
                UiExpense.fromDomainModel(it)
            }
        )
    }
}
