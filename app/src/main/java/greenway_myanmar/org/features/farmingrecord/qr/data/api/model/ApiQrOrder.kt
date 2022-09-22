package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import androidx.compose.runtime.Composer.Companion.Empty
import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Crop
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season

data class ApiQrOrder(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("qr")
    val qrInfo: ApiQrInfo? = null,
    @SerializedName("farm")
    val farm: ApiFarm? = null,
    @SerializedName("season")
    val season: ApiSeason? = null,
    @SerializedName("start_date")
    val startDate: String? = null,
    @SerializedName("end_date")
    val endDate: String? = null,
    @SerializedName("latest_status")
    val latestStatus: ApiQrOrderStatusDetail? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("qr_id_number")
    val qrIdNumber: String? = null,
    @SerializedName("qr_url")
    val qrUrl: String? = null,
    @SerializedName("statues")
    val statuses: List<ApiQrOrderStatusDetail>? = null
) {
    fun toDomain(): QrOrder {
        return QrOrder(
            id = id.orEmpty(),
            farm = farm?.toDomain() ?: ApiFarm.Empty.toDomain(),
            season = season?.toDomain() ?: ApiSeason.Empty.toDomain(),
            qrUrl = qrUrl.orEmpty(),
            qrIdNumber = qrIdNumber.orEmpty(),
            quantity = quantity ?: 0,
            latestStatus = latestStatus?.toDomain()
                ?: statuses?.last()?.toDomain()
                ?: throw IllegalStateException("latest order status should not be null!"),
            statuses = statuses.orEmpty().map { it.toDomain() }
        )
    }
}


