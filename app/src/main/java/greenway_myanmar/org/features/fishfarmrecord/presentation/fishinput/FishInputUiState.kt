package greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

data class FishInputUiState(
    val fish: UiFish? = null,
    val species: String? = null,

    val fishValidationError: Text? = null,

    val submitted: Boolean = false
)