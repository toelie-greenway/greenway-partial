package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.addeditfarm

import android.net.Uri
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmOwnership

data class AddEditFarmUiState(
    val farmName: String? = null,
    val farmArea: String? = null,
    val farmMeasurement: AreaMeasurement? = null,
    val farmImageUri: Uri? = null,
    val farmOwnership: UiFarmOwnership? = null,
    val inputFarmDetail: Boolean = false,
    val plotId: String? = null,
    val farmDepth: String? = null,

    val farmNameError: Text? = null,
    val farmAreaError: Text? = null,
    val farmOwnershipError: Text? = null,

    val farmUploadState: FarmUploadState = LoadingState.Idle
) {
    data class FarmInput(
        val farmName: String,
        val farmArea: UiArea,
        val farmImageUri: Uri? = null,
        val farmOwnership: UiFarmOwnership,
        val isDetail: Boolean = false,
        val uPaingNumber: String? = null,
        val farmDepth: String? = null,
    )

    data class AddEditFarmResult(
        val farmId: String
    )
}

typealias FarmUploadState = LoadingState<AddEditFarmUiState.AddEditFarmResult>