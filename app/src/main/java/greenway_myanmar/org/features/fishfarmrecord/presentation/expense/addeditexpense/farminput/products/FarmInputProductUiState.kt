package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProductCategory
import kotlinx.coroutines.flow.Flow

internal typealias ProductsUiState = LoadingState<List<UiFarmInputProduct>>

internal typealias CategoriesUiState = LoadingState<List<UiFarmInputProductCategory>>

data class FarmInputProductUiState(
    val query: String? = null,
    val category: UiFarmInputProductCategory? = null
)

data class UiFarmInputProductFilter(
    val query: String? = null,
    val category: UiFarmInputProductCategory? = null
) {
    fun <T> search(f: (String?, String?) -> Flow<T>): Flow<T> {
        return f(query, category?.id)
    }
}