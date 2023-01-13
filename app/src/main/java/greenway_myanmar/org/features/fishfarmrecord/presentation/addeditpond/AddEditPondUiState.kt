package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditpond

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiPondOwnership

data class AddEditPondUiState(
    val pondName: String? = null,
    val pondArea: String? = null,
    val pondOwnership: UiPondOwnership? = null,
    val isDetail: Boolean = false,
    val uPaingNumber: String? = null,
    val pondDepth: String? = null,

    val pondNameError: Text? = null,
    val createdPond: CreatedPond? = null,
) {
    val showUPaingNumber: Boolean = isDetail
    val showPondDepth: Boolean = isDetail

    data class CreatedPond(
        val pondId: String
    )
}