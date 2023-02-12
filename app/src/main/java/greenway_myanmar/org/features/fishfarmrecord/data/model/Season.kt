package greenway_myanmar.org.features.fishfarmrecord.data.model

import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeason
import greenway_myanmar.org.util.toInstantOrNow
import java.math.BigDecimal

fun NetworkSeason.asEntity(farmId: String) = FfrSeasonEntity(
    id = id,
    seasonName = season.orEmpty(),
    seasonStartDate = start_date.toInstantOrNow(),
    fishes = fish_types.orEmpty().map { it.asEntity() },
    company = company?.asEntity(),
    totalExpenses = BigDecimal.valueOf(total_cost ?: 0.0),
    loan = loan?.asEntity(),
    depth = area.depth,
    location = mapLocationOrNull(area.lat, area.lon),
    coordinates = area.measurement?.map { LatLng(it.lat, it.lng) },
    area = area.acre,
    measuredArea = area.measured_acre?.toDoubleOrNull(),
    measuredType = area.measurement_type,
    isEnd = is_end ?: false,
    isHarvested = is_harvest ?: false,
    farmId = farmId
)

private fun mapLocationOrNull(lat: Double?, lng: Double?): LatLng? {
    if (lat == null || lng == null) return null

    return LatLng(lat, lng)
}