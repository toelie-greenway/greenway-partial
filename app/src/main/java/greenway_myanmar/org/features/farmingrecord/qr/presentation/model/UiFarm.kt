package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FARM_DEFAULT_AREA_UNIT
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.QrOrderStatusItemUiState.PlaceholderItem.id
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.vo.SingleListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiFarm(
    val id: String,
    val name: String,
    val area: Double,
    val areaUnit: String,
    val images: List<String>,
    val location: String,
    val latitude: Double? = null,
    val longitude: Double? = null
) : SingleListItem, Parcelable {
    override val itemId: Long
        get() = id.toLong()
    override val displayText: String
        get() = name

    val displayArea: String
        get() = MyanmarZarConverter.toMyanmarNumber(area) + " " + userFriendlyFarmUnit()

    val image: String
        get() = images.getOrNull(0).orEmpty()

    private fun userFriendlyFarmUnit() = if (areaUnit == "acre") "ဧက" else ""

    fun hasLatLng() = latitude != null && longitude != null

    companion object {
        fun fromDomain(domainEntity: Farm): UiFarm {
            return UiFarm(
                id = domainEntity.id,
                name = domainEntity.name,
                area = domainEntity.area,
                areaUnit = domainEntity.areaUnit,
                images = domainEntity.images,
                location = domainEntity.location,
                latitude = domainEntity.latitude,
                longitude = domainEntity.longitude
            )
        }
    }
}