package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseSummary
import java.math.BigDecimal

data class UiExpenseSummary(
    val totalExpense: BigDecimal,
    val categoryExpenses: List<UiCategoryExpense>
) {
    companion object {
        fun fromDomainModel(domainModel: ExpenseSummary) = UiExpenseSummary(
            totalExpense = domainModel.totalExpense,
            categoryExpenses = domainModel.categoryExpenses.map {
                UiCategoryExpense.fromDomainModel(it)
            }
        )
    }
}