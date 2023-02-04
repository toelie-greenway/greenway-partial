package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.domain.model.asStringOrNull
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.EntityIdGenerator.generateIdIfRequired
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveSeasonUseCase
import kotlinx.datetime.Instant
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "ffr_seasons"
)
data class FfrSeasonEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("location")
    val location: LatLng? = null,
    @ColumnInfo("coordinates")
    val coordinates: List<LatLng>? = null,
    @ColumnInfo("area")
    val area: Double,
    @ColumnInfo("measured_area")
    val measuredArea: Double? = null,
    @ColumnInfo("measured_type")
    val measuredType: String? = null,
    @ColumnInfo("season_name")
    val seasonName: String,
    @ColumnInfo("season_start_date")
    val seasonStartDate: Instant,
    @ColumnInfo("fishes")
    val fishes: List<FfrFishEntity> = emptyList(),
    @Embedded(prefix = "company_")
    val company: FFrContractFarmingCompanyEntity? = null,
    @ColumnInfo("total_expenses")
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    @Embedded(prefix = "loan_")
    val loan: FfrLoanEntity? = null,
    @ColumnInfo("pending_action")
    val pendingAction: PendingAction = PendingAction.NOTHING
) {
    companion object {
        fun from(request: SaveSeasonUseCase.SaveSeasonRequest, pendingAction: PendingAction) =
            FfrSeasonEntity(
                id = generateIdIfRequired(request.id),
                location = request.farmMeasurement.location,
                coordinates = request.farmMeasurement.coordinates,
                area = request.farmMeasurement.area.value,
                measuredArea = request.farmMeasurement.measuredArea?.value,
                measuredType = request.farmMeasurement.measuredType.asStringOrNull(),
                seasonName = request.seasonName,
                seasonStartDate = request.seasonStartDate,
                fishes = request.fishes.map {
                    FfrFishEntity.fromDomainModel(it)
                },
                company = request.company?.let { FFrContractFarmingCompanyEntity.fromDomainModel(it) },
                loan = request.loan?.let { FfrLoanEntity.fromDomainModel(it) },
                pendingAction = pendingAction
            )
    }
}

fun FfrSeasonEntity.asDomainModel() = Season(
    id = id,
    name = seasonName,
    startDate = seasonStartDate,
    farmMeasurement = FarmMeasurement(
        location = location,
        coordinates = coordinates,
        area = Area.acre(area),
        measuredArea = Area.acreOrNull(measuredArea),
        measuredType = AreaMeasureMethod.fromStringOrNull(measuredType)
    ),
    totalExpenses = totalExpenses,
    fishes = mapFishes(fishes),
    company = company?.asDomainModel(),
    loan = loan?.asDomainModel()
)

private fun mapFishes(fishes: List<FfrFishEntity>) =
    fishes.map(FfrFishEntity::asDomainModel)