package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class PopulatedFcrRecord(
    @Embedded
    val fcrRecord: FfrFcrRecordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "record_id"
    )
    val ratios: List<FfrFcrEntity>
)