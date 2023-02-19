package greenway_myanmar.org.features.fishfarmrecord.domain.model

import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.util.Image

data class Farm(
    val id: String,
    val name: String,
    val images: List<Image>? = null,
    val openingSeason: Season? = null,
    val measurement: FarmMeasurement,
    val ownership: FarmOwnership,
    val plotId: String? = null,
    val pendingAction: PendingAction? = null
)