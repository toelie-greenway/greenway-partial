package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.labourcost

sealed class LabourCostInputEvent {
    data class OnLabourQuantityChanged(val quantity: String) : LabourCostInputEvent()
    data class OnLabourCostChanged(val cost: String) : LabourCostInputEvent()
    data class OnFamilyMemberQuantityChanged(val quantity: String) : LabourCostInputEvent()
    data class OnFamilyMemberCostChanged(val cost: String) : LabourCostInputEvent()
    object OnSubmit : LabourCostInputEvent()
}
