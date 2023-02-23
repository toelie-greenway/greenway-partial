package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fcr
import java.math.BigDecimal

data class UiFcr(
    val fish: UiFish,
    val feedWeight: BigDecimal,
    val gainWeight: BigDecimal,
    val ratio: BigDecimal
) {
    companion object {
        fun fromDomainModel(domainModel: Fcr) = UiFcr(
            fish = UiFish.fromDomain(domainModel.fish),
            feedWeight = domainModel.feedWeight,
            gainWeight = domainModel.gainWeight,
            ratio = domainModel.ratio
        )
    }
}