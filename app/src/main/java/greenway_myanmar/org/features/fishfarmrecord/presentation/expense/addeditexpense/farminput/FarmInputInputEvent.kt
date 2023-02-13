package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct

sealed class FarmInputInputEvent {
    data class OnFarmInputProductChanged(val product: UiFarmInputProduct) : FarmInputInputEvent()
    data class OnUsedAmountChanged(val amount: String) : FarmInputInputEvent()
    data class OnUsedUnitSelectionChanged(val position: Int) : FarmInputInputEvent()
    data class OnUsedUnitPriceChanged(val unitPrice: String) : FarmInputInputEvent()
    data class OnFingerlingWeightChanged(val weight: String) : FarmInputInputEvent()
    data class OnFingerlingSizeChanged(val size: String) : FarmInputInputEvent()
    data class OnFingerlingAgeChanged(val age: String) : FarmInputInputEvent()
    object OnSubmit : FarmInputInputEvent()
}