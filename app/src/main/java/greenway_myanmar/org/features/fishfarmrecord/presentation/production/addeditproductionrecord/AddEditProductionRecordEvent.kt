package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import java.time.LocalDate

sealed class AddEditProductionRecordEvent {
    data class OnDateChanged(val date: LocalDate) : AddEditProductionRecordEvent()
    data class OnWeightChanged(val fishId: String, val size: UiFishSize, val weight: String?) :
        AddEditProductionRecordEvent()

    data class OnPriceChanged(val fishId: String, val size: UiFishSize, val price: String?) :
        AddEditProductionRecordEvent()

    object AllInputErrorShown : AddEditProductionRecordEvent()
    object OnSubmit : AddEditProductionRecordEvent()
}
