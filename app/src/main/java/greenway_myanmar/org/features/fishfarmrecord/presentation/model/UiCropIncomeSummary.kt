package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncomeSummary
import java.math.BigDecimal

data class UiCropIncomeSummary(
    val totalIncome: BigDecimal,
    val cropIncomes: List<UiCropIncome>
) {
    companion object {
        fun fromDomainModel(domainModel: CropIncomeSummary) = UiCropIncomeSummary(
            totalIncome = domainModel.totalIncome,
            cropIncomes = domainModel.cropIncomes.map {
                UiCropIncome.fromDomainModel(it)
            }
        )
    }
}