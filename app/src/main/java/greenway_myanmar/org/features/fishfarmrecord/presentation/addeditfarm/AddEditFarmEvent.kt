package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditfarm

import android.net.Uri
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmOwnership

sealed class AddEditFarmEvent {
    data class OnDetailChanged(val showDetail: Boolean) : AddEditFarmEvent()
    data class OnFarmNameChanged(val name: String?) : AddEditFarmEvent()
    data class OnFarmAreaChanged(val area: String?) : AddEditFarmEvent()
    data class OnFarmAreaMeasurementChanged(val measurement: AreaMeasurement) : AddEditFarmEvent()
    data class OnFarmImageChanged(val imageUri: Uri) : AddEditFarmEvent()
    data class OnFarmOwnershipChanged(val ownership: UiFarmOwnership?) : AddEditFarmEvent()
    data class OnFarmDepthChanged(val depth: String?) : AddEditFarmEvent()
    data class OnFarmUPaingNumberChanged(val uPaingNumber: String?) : AddEditFarmEvent()
    object OnCreatedFarmHandled : AddEditFarmEvent()
    object OnSubmit : AddEditFarmEvent()
}
