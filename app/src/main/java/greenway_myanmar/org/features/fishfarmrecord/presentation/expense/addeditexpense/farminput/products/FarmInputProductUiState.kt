package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductFilter
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProductCategory

internal typealias ProductsUiState = LoadingState<List<UiFarmInputProduct>>

internal typealias CategoriesUiState = LoadingState<List<UiFarmInputProductCategory>>

data class FarmInputProductFilterUiState(
    val query: String? = null,
    val category: UiFarmInputProductCategory? = null
)

data class FarmInputProductUiState(
    val filter: FarmInputProductFilterUiState? = null
)

fun FarmInputProductFilterUiState.toDomainModel() = FarmInputProductFilter(
    categoryId = category?.id,
    query = query
)