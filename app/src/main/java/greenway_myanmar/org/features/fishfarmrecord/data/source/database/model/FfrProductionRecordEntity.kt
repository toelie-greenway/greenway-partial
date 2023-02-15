package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecord
import kotlinx.datetime.Instant
import java.math.BigDecimal

@Entity(
    tableName = "ffr_production_records"
)
data class FfrProductionRecordEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("season_id")
    val seasonId: String,
    val date: Instant,
    @ColumnInfo("total_price")
    val totalPrice: BigDecimal,
    val productions: List<FfrProductionPerFishTypeEntity>,
    val note: String,
    @ColumnInfo(name = "pending_action")
    val pendingAction: PendingAction = PendingAction.NOTHING,
)

fun FfrProductionRecordEntity.asDomainMode() = ProductionRecord(
    id = id,
    date = date,
    productions = productions.map(FfrProductionPerFishTypeEntity::asDomainModel),
    note = note
)