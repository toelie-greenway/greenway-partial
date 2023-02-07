package greenway_myanmar.org.features.fishfarmrecord.data.model

import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm

fun NetworkFarm.asEntity() = FfrFarmEntity(
    id = id,
    name = name,
    ownership = ownership,
    imageUrls = photos,
    plotId = plot_id,
    depth = area.depth,
    location = LatLng(lat ?: 0.0, lon ?: 0.0),
    coordinates = area.measurement?.map { LatLng(it.lat, it.lng) },
    area = area.acre,
    measuredArea = area.measured_acre,
    measuredType = area.measurement_type
)
