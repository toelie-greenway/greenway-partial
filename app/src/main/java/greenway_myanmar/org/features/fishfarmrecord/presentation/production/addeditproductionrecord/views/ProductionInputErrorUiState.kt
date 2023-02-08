package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord.views

import greenway_myanmar.org.common.domain.entities.Text

data class ProductionInputErrorUiState(
    val weightError: Text? = null,
    val priceError: Text? = null,
)