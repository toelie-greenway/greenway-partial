package greenway_myanmar.org.features.fishfarmrecord.domain.model

import com.greenwaymyanmar.core.domain.model.UnitOfMeasurement

data class FarmInputProduct(
    val id: String,
    val name: String,
    val thumbnail: String,
    val description: String,
    val units: List<UnitOfMeasurement>
)