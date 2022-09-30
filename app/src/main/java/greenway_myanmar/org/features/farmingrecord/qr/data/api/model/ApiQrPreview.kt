package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.*
import greenway_myanmar.org.features.farmingrecord.qr.presentation.QrOrderStatusItemUiState.PlaceholderItem.id
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils
import java.time.Instant

data class ApiQrPreview(
    @SerializedName("activities")
    val activities: List<ApiFarmActivity>? = null,
    @SerializedName("farm")
    val farm: ApiFarm? = null,
    @SerializedName("request_qr")
    val qrInfo: ApiQrInfo? = null,
    @SerializedName("season")
    val season: ApiSeason? = null,
    @SerializedName("temp_qr_id_number")
    val qrIdNumber: String? = null,
    @SerializedName("temp_qr_url")
    val qrUrl: String? = null,
    @SerializedName("user")
    val user: ApiQrUser? = null,
    @SerializedName("harvest_date")
    val latestHarvestedDate: String? = null
) {
    fun toDomainQrDetail() = QrDetail(
        id = qrInfo?.qrId.orEmpty(),
        qrInfo = qrInfo?.toDomain() ?: QrInfo.Empty,
        qrUrl = qrUrl.orEmpty(),
        qrIdNumber = qrIdNumber.orEmpty(),
        user = user?.toDomain() ?: QrUser.Empty,
        farm = farm?.toDomain() ?: Farm.Empty,
        season = season?.toDomain(DateUtils.parseIsoDateTimeToInstantOrNull(latestHarvestedDate))
            ?: Season.Empty,
        farmActivities = activities.orEmpty().map { it.toDomain() })
}