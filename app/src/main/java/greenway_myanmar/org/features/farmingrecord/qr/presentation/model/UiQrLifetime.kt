package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrLifetime
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrQuantity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.QrOrderStatusItemUiState.PlaceholderItem.id
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.vo.SingleListItem
import java.text.NumberFormat

data class UiQrLifetime(
    val month: Int
) : SingleListItem {
    override val itemId: Long
        get() = month.toLong()
    override val displayText: String
        get() = NumberFormat.getInstance(MyanmarZarConverter.getLocale()).format(month) + " လ"

    companion object {
        fun fromDomain(domainEntity: QrLifetime): UiQrLifetime {
            return UiQrLifetime(
                month = domainEntity.month
            )
        }
    }
}