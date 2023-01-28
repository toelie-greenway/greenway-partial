package greenway_myanmar.org.features.fishfarmrecord.domain.model

import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod

data class FarmMeasurement (
    val location: LatLng? = null,
    val coordinates: List<LatLng>? = null,
    val area: Area,
    val measuredArea: Area? = null,
    val measuredType: AreaMeasureMethod? = null
)