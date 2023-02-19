package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncome
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class UiCropIncome(
    val id: String,
    val date: Instant,
    val income: BigDecimal,
    val crop: UiCrop
) {
    companion object {
        fun fromDomainModel(domainModel: CropIncome) = UiCropIncome(
            id = domainModel.id,
            date = domainModel.date,
            income = domainModel.income,
            crop =  UiCrop.fromDomainModel(domainModel.crop)
        )
    }
}