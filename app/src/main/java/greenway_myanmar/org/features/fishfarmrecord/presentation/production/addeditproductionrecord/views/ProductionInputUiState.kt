package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord.views

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import java.math.BigDecimal

data class ProductionInputUiState(
    val fish: UiFish,
    val size: ProductionSizeInputUiState
) {
    fun isInputValid() = size.isInputValid()
}

data class ProductionSizeInputUiState(
    val size: UiFishSize,
    val weight: BigDecimal,
    val price: BigDecimal,
) {

    fun isInputValid() = weight > BigDecimal.ZERO && price > BigDecimal.ZERO

    fun getWeightErrorOrNull(error: Text?) = if (weight <= BigDecimal.ZERO) {
        error
    } else {
        null
    }

    fun getPriceErrorOrNull(error: Text?) = if (price <= BigDecimal.ZERO) {
        error
    } else {
        null
    }
}


//fun FcrRatioInputUiState.asDomainModel() = Fcr (
//    fish = fish.asDomainModel(),
//    feedWeight = feedWeight,
//    gainWeight = gainWeight
//)