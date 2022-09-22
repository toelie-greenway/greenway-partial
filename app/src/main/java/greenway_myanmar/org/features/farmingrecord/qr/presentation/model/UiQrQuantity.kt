package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrQuantity
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.vo.SingleListItem
import java.text.NumberFormat

data class UiQrQuantity(
    val id: String,
    val quantity: Int,
    val price: String
) : SingleListItem {
    override val itemId: Long
        get() = id.toLong()
    override val displayText: String
        get() = NumberFormat.getInstance(MyanmarZarConverter.getLocale()).format(quantity)

    val formattedPrice: String
        get() = price// NumberFormat.getInstance(MyanmarZarConverter.getLocale()).format(price)

    companion object {
        fun fromDomain(domainEntity: QrQuantity): UiQrQuantity {
            return UiQrQuantity(
                id = domainEntity.quantity.toString(),
                quantity = domainEntity.quantity,
                price = domainEntity.price
            )
        }
    }
}