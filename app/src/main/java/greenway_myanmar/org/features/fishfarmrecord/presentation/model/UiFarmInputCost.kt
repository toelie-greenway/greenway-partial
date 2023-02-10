package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputCost
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class UiFarmInputCost(
    val productId: String,
    val productName: String,
    val productThumbnail: String,
    var amount: Double = 0.0,
    var unit: String = "",
    var unitPrice: BigDecimal,
    var totalCost: BigDecimal,
    val fingerlingWeight: BigDecimal? = null,
    val fingerlingSize: BigDecimal? = null,
    val fingerlingAge: BigDecimal? = null,
) : Parcelable

fun UiFarmInputCost.asDomainModel() = FarmInputCost(
    productId = productId,
    productName = productName,
    amount = amount,
    unit = unit,
    unitPrice = unitPrice,
    fingerlingWeight = fingerlingWeight,
    fingerlingSize = fingerlingSize,
    fingerlingAge = fingerlingAge,
    totalCost = totalCost
)