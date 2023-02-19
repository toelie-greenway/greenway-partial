package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import greenway_myanmar.org.util.toInstantOrNow
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFcrRecord(
    val id: String,
    val date: String? = null,
    val record: List<NetworkFcr>? = null
)

fun NetworkFcrRecord.asDomainModel() = FcrRecord(
    id = id,
    date = date.toInstantOrNow(),
    ratios = record.orEmpty().map {
        it.asDomainModel()
    }
)