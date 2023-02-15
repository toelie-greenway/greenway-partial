package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.addeditcropincome

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCrop
import java.time.LocalDate

data class AddEditCropIncomeUiState(
    val date: LocalDate = LocalDate.now(),
    val crop: UiCrop? = null,
    val price: String? = null,
    val cropError: Text? = null,
    val priceError: Text? = null,
)