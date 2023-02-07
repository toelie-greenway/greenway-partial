package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import com.google.android.gms.maps.model.LatLng
import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement

data class UiFarmMeasurement(
    val location: LatLng? = null,
    val coordinates: List<LatLng>? = null,
    val area: UiArea,
    val measuredArea: UiArea? = null,
    val measuredType: AreaMeasureMethod? = null,
    val depth: Double?
) {
    companion object {
        fun fromDomain(domainEntity: FarmMeasurement) = UiFarmMeasurement(
            location = domainEntity.location,
            coordinates = domainEntity.coordinates,
            area = UiArea.fromDomain(domainEntity.area),
            measuredArea = mapArea(domainEntity.measuredArea),
            measuredType = domainEntity.measuredType,
            depth = domainEntity.depth
        )

        private fun mapArea(area: Area?): UiArea? {
            if (area == null) return null

            return UiArea.fromDomain(area)
        }
    }
}
