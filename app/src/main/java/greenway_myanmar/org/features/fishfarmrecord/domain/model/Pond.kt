package greenway_myanmar.org.features.fishfarmrecord.domain.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season

data class Pond(
    val id: String,
    val name: String,
    val images: List<String>,
    val ongoingSeason: Season? = null,
    val area: Area
)
