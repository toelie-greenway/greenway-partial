package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecord
import greenway_myanmar.org.util.toInstantOrNow

@kotlinx.serialization.Serializable
data class NetworkProductionRecord(
    val id: String? = null,
    val season_id: String? = null,
    val date: String? = null,
    val total: Double? = null,
    val productions: List<NetworkProductionPerFishType>? = null,
    val note: String? = null
)

fun NetworkProductionRecord.asDomainModel() = ProductionRecord(
    id = id.orEmpty(),
    date = date.toInstantOrNow(),
    productions = productions.orEmpty().map {
        it.asDomainModel()
    },
    note = note.orEmpty(),
)