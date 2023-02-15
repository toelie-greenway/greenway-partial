package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Expense
import kotlinx.datetime.Instant
import java.math.BigDecimal

@Entity(
    tableName = "ffr_expenses"
)
data class FfrExpenseEntity(
    @PrimaryKey
    val id: String,
    val date: Instant,
    @ColumnInfo(name = "labour_quantity")
    val labourQuantity: Int? = null,
    @ColumnInfo(name = "labour_cost")
    val labourCost: BigDecimal? = null,
    @ColumnInfo(name = "family_quantity")
    val familyQuantity: Int? = null,
    @ColumnInfo(name = "family_cost")
    val familyCost: BigDecimal? = null,
    @ColumnInfo(name = "machinery_cost")
    val machineryCost: BigDecimal? = null,
    val inputs: List<FfrFarmInputExpenseEntity>? = null,
    @ColumnInfo(name = "total_cost")
    val totalCost: BigDecimal? = null,
    val photos: List<String>? = null,
    val remark: String? = null,
    @ColumnInfo(name = "season_id")
    val seasonId: String,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    @ColumnInfo(name = "pending_action")
    val pendingAction: PendingAction = PendingAction.NOTHING,
)

fun FfrExpenseEntity.asDomainModel() = Expense(
    id = id,
    date = date,
    labourQuantity = labourQuantity,
    labourCost = labourCost,
    familyQuantity = familyQuantity,
    familyCost = familyCost,
    machineryCost = machineryCost,
    totalCost = totalCost,
    photos = photos,
    remark = remark,
    inputs = inputs.orEmpty().map(FfrFarmInputExpenseEntity::asDomainModel)
)