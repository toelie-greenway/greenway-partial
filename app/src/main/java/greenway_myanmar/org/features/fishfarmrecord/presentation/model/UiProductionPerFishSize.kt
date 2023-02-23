package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.content.Context
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFishSize
import java.math.BigDecimal

data class UiProductionPerFishSize(
    val fishSize: UiFishSize,
    val weight: BigDecimal,
    val price: BigDecimal
) {

    fun formattedWeight(context: Context) =
        context.getString(R.string.formatted_viss, numberFormat.format(weight))

    fun formattedPrice(context: Context) =
        context.getString(R.string.formatted_kyat, numberFormat.format(price))

    companion object {
        fun fromDomainModel(domainModel: ProductionPerFishSize) = UiProductionPerFishSize(
            fishSize = UiFishSize.fromDomainModel(domainModel.fishSize),
            weight = domainModel.weight,
            price = domainModel.price
        )
    }
}