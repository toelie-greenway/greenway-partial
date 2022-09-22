package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.QrOrderStatusItemUiState.PlaceholderItem.id
import greenway_myanmar.org.vo.SingleListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiFarm(
    val id: String,
    val name: String
) : SingleListItem, Parcelable {
    override val itemId: Long
        get() = id.toLong()
    override val displayText: String
        get() = name

    companion object {
        fun fromDomain(domainEntity: Farm): UiFarm {
            return UiFarm(
                id = domainEntity.id,
                name = domainEntity.name
            )
        }
    }
}