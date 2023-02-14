package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordRequest
import kotlinx.datetime.Instant
import java.util.*

@Entity(
    tableName = "ffr_fcr_record"
)
data class FfrFcrRecordEntity(
    @PrimaryKey
    val id: String,
    val date: Instant,
    @ColumnInfo("season_id")
    val seasonId: String,
    val pendingAction: PendingAction = PendingAction.NOTHING
) {
    companion object {
        fun from(request: SaveFcrRecordRequest, pendingAction: PendingAction) = FfrFcrRecordEntity(
            id = generateIdIfRequired(request.id),
            date = request.date,
            seasonId = request.seasonId,
            pendingAction = pendingAction
        )

        private fun generateIdIfRequired(id: String?) =
            if (id.isNullOrEmpty()) UUID.randomUUID().toString() else id
    }
}