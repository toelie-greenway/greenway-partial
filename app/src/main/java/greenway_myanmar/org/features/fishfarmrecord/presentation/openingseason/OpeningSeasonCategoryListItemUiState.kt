package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import java.math.BigDecimal
import java.time.Instant

data class OpeningSeasonCategoryListItemUiState(
    val categoryId: String,
    val categoryName: String,
    val lastRecordDate: Instant? = null,
    val totalCategoryExpense: BigDecimal
) {
    val hasRecord: Boolean = lastRecordDate != null
}
