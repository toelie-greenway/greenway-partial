package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season

data class ApiQrOrder(
    @SerializedName("farm")
    val farm: ApiFarm? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("lastest_status")
    val latestStatus: ApiQrOrderStatusDetail? = null,
    @SerializedName("qr_id_number")
    val codeNumber: String? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("season")
    val season: ApiSeason? = null
) {
    fun toDomain(): QrOrder {
        return QrOrder(
            id = id.orEmpty(),
            farm = Farm(
                id = farm?.id.orEmpty(),
                name = farm?.name.orEmpty()
            ),
            season = Season(
                id = season?.id.orEmpty(),
                seasonName = season?.seasonName.orEmpty()
            ),
            qrUrl = "//TODO:",
            quantity = quantity ?: 0,
            orderStatusDetail = latestStatus?.toDomain()
                ?: throw IllegalStateException("order status should not be null!")
        )
    }
}


