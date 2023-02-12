package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputExpense
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class UiFarmInputCost(
    val productId: String,
    val productName: String,
    val productThumbnail: String,
    var amount: BigDecimal,
    var unit: String = "",
    var unitPrice: BigDecimal,
    var totalCost: BigDecimal,
    val fingerlingWeight: BigDecimal? = null,
    val fingerlingSize: BigDecimal? = null,
    val fingerlingAge: BigDecimal? = null,
) : Parcelable {
    companion object {
        fun fromDomainModel(domainModel: FarmInputExpense ) = UiFarmInputCost(
            productId = domainModel.productId,
            productName = domainModel.productName,
            productThumbnail = domainModel.productThumbnail,
            amount = domainModel.amount,
            unit = domainModel.unit,
            unitPrice = domainModel.unitPrice,
            totalCost = domainModel.totalCost,
            fingerlingWeight = domainModel.fingerlingWeight,
            fingerlingSize = domainModel.fingerlingSize,
            fingerlingAge = domainModel.fingerlingAge,
        )
    }
}

fun UiFarmInputCost.asDomainModel() = FarmInputExpense(
    productId = productId,
    productName = productName,
    productThumbnail = productThumbnail,
    amount = amount,
    unit = unit,
    unitPrice = unitPrice,
    fingerlingWeight = fingerlingWeight,
    fingerlingSize = fingerlingSize,
    fingerlingAge = fingerlingAge,
    totalCost = totalCost
)