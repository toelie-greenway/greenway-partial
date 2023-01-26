package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import com.greenwaymyanmar.core.domain.model.UnitOfMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProduct
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFarmInputProduct(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val units: List<String>? = emptyList()
)

fun NetworkFarmInputProduct.asDomainModel() = FarmInputProduct(
    id = id,
    name = name.orEmpty(),
    description = description.orEmpty(),
    thumbnail = thumbnail.orEmpty(),
    units = units.orEmpty().map { UnitOfMeasurement.fromString(it) }
)