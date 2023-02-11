package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import java.math.BigDecimal
import java.time.Instant

sealed class OpeningSeasonCategoryListItemUiState {
    data class CategoryItem(
        val categoryId: String,
        val categoryName: String,
        val lastRecordDate: Instant? = null,
        val totalCategoryExpense: BigDecimal
    ) : OpeningSeasonCategoryListItemUiState() {
        val hasRecord: Boolean = lastRecordDate != null
    }

    data class ProductionItem(
        val lastRecordDate: Instant? = null,
        val totalIncome: BigDecimal
    ) : OpeningSeasonCategoryListItemUiState() {
        val hasRecord: Boolean = lastRecordDate != null
    }

    object CloseItem : OpeningSeasonCategoryListItemUiState()
}
