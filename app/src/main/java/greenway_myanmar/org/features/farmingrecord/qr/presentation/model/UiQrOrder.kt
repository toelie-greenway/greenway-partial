package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.common.presentation.utils.CommonConstants.MYANMAR_NUMBER_FORMAT
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils
import java.time.Instant
import java.time.LocalDate

data class UiQrOrder(
    val id: String,
    val farmName: String,
    val cropName: String,
    val seasonName: String,
    val orderStatusDetail: UiQrOrderStatusDetail,
    val qrUrl: String,
    val quantity: Int,
    val harvestedDate: Instant
) {

    val formattedHarvestedDate: String = DateUtils.format(harvestedDate, "yyyy")
    val orderStatusDate = DateUtils.format(orderStatusDetail.createdAt, "d/M/yyyy")
    val formattedQuantity: String = MYANMAR_NUMBER_FORMAT.format(quantity)

    companion object {
        fun fromDomain(domainEntity: QrOrder): UiQrOrder {
            return UiQrOrder(
                id = domainEntity.id,
                farmName = domainEntity.farm.name,
                seasonName = domainEntity.season.seasonName,
                cropName = domainEntity.season.crop.title,
                qrUrl = domainEntity.qrUrl,
                quantity = domainEntity.quantity,
                orderStatusDetail = UiQrOrderStatusDetail.fromDomain(domainEntity.latestStatus),
                harvestedDate = domainEntity.harvestedDate
            )
        }
    }
}