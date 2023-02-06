package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.Embedded
import androidx.room.Relation
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord

data class PopulatedFcrRecord(
    @Embedded
    val fcrRecord: FfrFcrRecordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "record_id"
    )
    val ratios: List<FfrFcrEntity>
)

fun PopulatedFcrRecord.asDomainModel() = FcrRecord(
    id = fcrRecord.id,
    date = fcrRecord.date,
    ratios = ratios.map(FfrFcrEntity::asDomainModel)
)