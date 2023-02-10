package greenway_myanmar.org.features.fishfarmrecord.data.model

import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm
import greenway_myanmar.org.util.toInstantOrNow

fun NetworkFarm.asEntity() = FfrFarmEntity(
    id = id,
    name = name,
    ownership = ownership,
    imageUrls = photos,
    plotId = plot_id,
    depth = area.depth,
    location = mapLocationOrNull(area.lat, area.lon),
    coordinates = area.measurement?.map { LatLng(it.lat, it.lng) },
    area = area.acre,
    measuredArea = area.measured_acre?.toDoubleOrNull(),
    measuredType = area.measurement_type,
    createdAt = created_at.toInstantOrNow()
)

private fun mapLocationOrNull(lat: Double?, lng: Double?): LatLng? {
    if (lat == null || lng == null) return null

    return LatLng(lat, lng)
}