package greenway_myanmar.org.features.fishfarmerrecordbook.domain.model

import com.greenwaymyanmar.core.domain.model.Area

data class Pond(
    val id: String,
    val name: String,
    val images: List<String>,
    val ongoingSeason: Season? = null,
    val area: Area? = null
)
