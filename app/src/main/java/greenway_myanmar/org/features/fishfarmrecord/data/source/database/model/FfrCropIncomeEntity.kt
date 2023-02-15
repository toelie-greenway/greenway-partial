package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncome
import kotlinx.datetime.Instant
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "ffr_crop_incomes"
)
data class FfrCropIncomeEntity(
    @PrimaryKey
    val id: String,
    val date: Instant,
    val income: BigDecimal,
    @Embedded(prefix = "crop_")
    val crop: FFrCropEntity,
    @ColumnInfo("season_id")
    val seasonId: String,
    val pendingAction: PendingAction = PendingAction.NOTHING
)

fun FfrCropIncomeEntity.asDomainModel() = CropIncome(
    id = id,
    date = date,
    income = income,
    crop = crop.asDomainModel()
)