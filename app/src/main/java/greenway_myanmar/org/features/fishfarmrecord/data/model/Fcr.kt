package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFcr
import greenway_myanmar.org.util.extensions.orZero

fun NetworkFcr.asEntity(recordId: String) = FfrFcrEntity(
    fish = fish?.asEntity() ?: throw IllegalStateException("Fish must not be null!"),
    feedWeight = total_feed.orZero(),
    gainWeight = total_weight_gain.orZero(),
    recordId = recordId
)

fun List<NetworkFcr>.asEntities(recordId: String) = this.map {
    it.asEntity(recordId)
}