package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.domain.model.asStringOrNull
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.EntityIdGenerator.generateIdIfRequired
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmAreaLatLng
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkFarmAreaRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkFarmRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area.Companion.acre
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area.Companion.acreOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmOwnership
import greenway_myanmar.org.features.fishfarmrecord.domain.model.asString
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase.SaveFarmRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "ffr_farms",
    foreignKeys = [
        ForeignKey(
            entity = FfrSeasonEntity::class,
            parentColumns = ["id"],
            childColumns = ["opening_season_id"]
        )
    ],
    indices = [
        Index("opening_season_id")
    ]
)
data class FfrFarmEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "ownership")
    val ownership: String,
    @ColumnInfo(name = "image_urls")
    val imageUrls: List<String>? = null,
    @ColumnInfo(name = "plot_id")
    val plotId: String? = null,
    @ColumnInfo(name = "depth")
    val depth: Double? = null,
    @ColumnInfo(name = "pending_action")
    val pendingAction: PendingAction = PendingAction.NOTHING,
    @ColumnInfo(name = "location")
    val location: LatLng? = null,
    @ColumnInfo(name = "coordinates")
    val coordinates: List<LatLng>? = null,
    @ColumnInfo(name = "area")
    val area: Double,
    @ColumnInfo(name = "measured_area")
    val measuredArea: Double? = null,
    @ColumnInfo(name = "measured_type")
    val measuredType: String? = null,
    @ColumnInfo(name = "opening_season_id")
    val openingSeasonId: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant
) {

    companion object {
        fun from(request: SaveFarmRequest, pendingAction: PendingAction) =
            FfrFarmEntity(
                id = generateIdIfRequired(request.id),
                name = request.name,
                ownership = request.ownership.asString(),
                imageUrls = mapImageUri(request.imageUri),
                plotId = request.plotId,
                depth = request.measurement.depth,
                pendingAction = pendingAction,
                location = request.measurement.location,
                coordinates = request.measurement.coordinates,
                area = request.measurement.area.value,
                measuredArea = request.measurement.measuredArea?.value,
                measuredType = request.measurement.measuredType.asStringOrNull(),
                createdAt = Clock.System.now()
            )

        private fun mapImageUri(imageUri: Uri?): List<String>? {
            if (imageUri == null) return null

            return listOf(imageUri.toString())
        }
    }
}

data class FarmAreaEntity(
    private val location: LatLng? = null,
    private val acre: Double,
    private val measurementType: String? = null,
    private val measuredAcre: Double? = null,
    private val depth: Double? = null,
    private val measurement: List<LatLng>? = null
)

fun FfrFarmEntity.asDomainModel(openingSeason: FfrSeasonEntity? = null) = Farm(
    id = id,
    name = name,
    images = imageUrls,
    measurement = FarmMeasurement(
        location = location,
        coordinates = coordinates,
        area = acre(area),
        measuredArea = acreOrNull(measuredArea),
        measuredType = AreaMeasureMethod.fromStringOrNull(measuredType),
        depth = depth
    ),
    plotId = plotId,
    ownership = FarmOwnership.fromString(ownership),
    openingSeason = openingSeason?.asDomainModel(),
    pendingAction = pendingAction
)


fun FfrFarmEntity.asNetworkRequestModel() = NetworkFarmRequest(
    name = name,
    ownership = ownership,
    photos = emptyList(), //TODO:
    plot_id = plotId,
    area = NetworkFarmAreaRequest(
        acre = area,
        measurement_type = measuredType,
        measured_acre = measuredArea,
        depth = depth,
        lat = location?.latitude,
        lon = location?.longitude,
        measurement = coordinates?.map {
            NetworkFarmAreaLatLng(
                lat = it.latitude,
                lng = it.longitude
            )
        }
    )
)