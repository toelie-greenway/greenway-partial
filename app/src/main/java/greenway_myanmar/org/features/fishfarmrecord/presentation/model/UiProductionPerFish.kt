package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.content.Context
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFish

data class UiProductionPerFish(
    val fish: UiFish,
    val productionsPerFishSize: List<UiProductionPerFishSize>
) {
    companion object {
        fun fromDomainModel(domainModel: ProductionPerFish) = UiProductionPerFish(
            fish = UiFish.fromDomain(domainModel.fish),
            productionsPerFishSize = domainModel.productionsPerFishSize.map(
                UiProductionPerFishSize.Companion::fromDomainModel
            )
        )
    }
}