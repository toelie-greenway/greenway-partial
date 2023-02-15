package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.addeditcropincome

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCrop
import java.time.LocalDate

sealed class AddEditCropIncomeEvent {
    data class OnDateChanged(val date: LocalDate) : AddEditCropIncomeEvent()
    data class OnCropChanged(val crop: UiCrop) : AddEditCropIncomeEvent()
    data class OnPriceChanged(val price: String) : AddEditCropIncomeEvent()
    object OnSubmit : AddEditCropIncomeEvent()
    object OnCropIncomeSavingErrorShown : AddEditCropIncomeEvent()
}
