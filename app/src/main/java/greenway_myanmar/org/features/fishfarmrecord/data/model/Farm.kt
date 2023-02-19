package greenway_myanmar.org.features.fishfarmrecord.data.model

import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm
import greenway_myanmar.org.util.Image
import greenway_myanmar.org.util.toInstantOrNow

fun NetworkFarm.asEntity() = FfrFarmEntity(
    id = id,
    name = name,
    ownership = ownership,
    images = photos.orEmpty().map {
        Image.from(it)
    },
    plotId = plot_id,
    depth = area.depth,
    location = mapLocationOrNull(area.lat, area.lon),
    coordinates = area.measurement?.map { LatLng(it.lat, it.lng) },
    area = area.acre,
    measuredArea = area.measured_acre?.toDoubleOrNull(),
    measuredType = area.measurement_type,
    createdAt = created_at.toInstantOrNow(),
    openingSeasonId = opening_season?.id
)

private fun mapLocationOrNull(lat: Double?, lng: Double?): LatLng? {
    if (lat == null || lng == null) return null

    return LatLng(lat, lng)
}