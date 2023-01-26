package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.labourcost

sealed class LabourCostInputEvent {
    data class OnLabourQuantityChanged(val quantity: String) : LabourCostInputEvent()
    data class OnLabourCostChanged(val cost: String) : LabourCostInputEvent()
    data class OnAnimalResourceQuantityChanged(val quantity: String) : LabourCostInputEvent()
    data class OnAnimalResourceCostChanged(val cost: String) : LabourCostInputEvent()
    data class OnFamilyMemberQuantityChanged(val quantity: String) : LabourCostInputEvent()
    data class OnFamilyMemberCostChanged(val cost: String) : LabourCostInputEvent()
    object OnSubmit : LabourCostInputEvent()
}
