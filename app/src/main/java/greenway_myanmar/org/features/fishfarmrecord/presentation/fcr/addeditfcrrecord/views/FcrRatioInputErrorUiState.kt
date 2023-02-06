package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views

import greenway_myanmar.org.common.domain.entities.Text

data class FcrRatioInputErrorUiState(
    val feedWeightError: Text? = null,
    val gainWeightError: Text? = null,
)