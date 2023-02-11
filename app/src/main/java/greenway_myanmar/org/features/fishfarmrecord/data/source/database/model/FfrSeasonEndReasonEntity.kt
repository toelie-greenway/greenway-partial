package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import kotlinx.serialization.Serializable

@Entity(
    tableName = "ffr_season_end_reasons"
)
@Serializable
data class FfrSeasonEndReasonEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val order: Int
)

fun FfrSeasonEndReasonEntity.asDomainModel() = SeasonEndReason(
    id = id,
    name = name
)