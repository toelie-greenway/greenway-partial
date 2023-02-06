package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fcr
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asDomainModel
import java.math.BigDecimal

data class FcrRatioInputUiState(
    val fish: UiFish,
    val feedWeight: BigDecimal,
    val gainWeight: BigDecimal,
) {

    fun isInputValid() = feedWeight > BigDecimal.ZERO && gainWeight > BigDecimal.ZERO

    fun getFeedWeightErrorOrNull(error: Text?) = if (feedWeight <= BigDecimal.ZERO) {
        error
    } else {
        null
    }

    fun getGainWeightErrorOrNull(error: Text?) = if (gainWeight <= BigDecimal.ZERO) {
        error
    } else {
        null
    }
}

fun FcrRatioInputUiState.asDomainModel() = Fcr (
    fish = fish.asDomainModel(),
    feedWeight = feedWeight,
    gainWeight = gainWeight
)