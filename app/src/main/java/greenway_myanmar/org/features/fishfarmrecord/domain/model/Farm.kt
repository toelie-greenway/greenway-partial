package greenway_myanmar.org.features.fishfarmrecord.domain.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season

data class Farm(
    val id: String,
    val name: String,
    val images: List<String>? = null,
    val ongoingSeason: Season? = null,
    val measurement: FarmMeasurement,
    val ownership: FarmOwnership,
    val plotId: String? = null
)