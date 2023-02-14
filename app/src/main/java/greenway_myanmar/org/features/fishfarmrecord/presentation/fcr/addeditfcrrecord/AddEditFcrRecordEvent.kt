package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord

import java.time.LocalDate

sealed class AddEditFcrRecordEvent {
    data class OnDateChanged(val date: LocalDate): AddEditFcrRecordEvent()
    data class OnFeedWeightChanged(val index: Int, val weight: String?) : AddEditFcrRecordEvent()
    data class OnGainWeightChanged(val index: Int, val weight: String?) : AddEditFcrRecordEvent()
    object AllInputErrorShown: AddEditFcrRecordEvent()
    object OnSavingFcrRecordErrorShown: AddEditFcrRecordEvent()
    object OnSubmit: AddEditFcrRecordEvent()
}
