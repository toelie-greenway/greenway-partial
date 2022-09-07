package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.common.presentation.utils.CommonConstants.MYANMAR_NUMBER_FORMAT
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils
import java.time.Instant

data class UiQrOrder(
    val id: String,
    val farmName: String,
    val cropName: String,
    val seasonName: String,
    val seasonCreatedDate: Instant,
    val orderStatusDetail: UiQrOrderStatusDetail,
    val qrUrl: String,
    val quantity: Int
) {

    val seasonYear = DateUtils.format(seasonCreatedDate, "yyyy")
    val orderStatusDate = DateUtils.format(orderStatusDetail.createdAt, "d/M/yyyy")
    val formattedQuantity: String = MYANMAR_NUMBER_FORMAT.format(quantity)

    companion object {
        fun fromDomain(domainEntity: QrOrder): UiQrOrder {
            return UiQrOrder(
                id = domainEntity.id,
                farmName = domainEntity.farm.name,
                seasonName = domainEntity.season.seasonName,
                seasonCreatedDate = Instant.now(), //TODO:
                cropName = "TODO",
                qrUrl = domainEntity.qrUrl,
                quantity = domainEntity.quantity,
                orderStatusDetail = UiQrOrderStatusDetail.fromDomain(domainEntity.orderStatusDetail),
            )
        }
    }
}