package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.domain.model.asStringOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area.Companion.acre
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area.Companion.acreOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmOwnership
import greenway_myanmar.org.features.fishfarmrecord.domain.model.asString
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase.SaveFarmRequest
import java.util.*

@Entity(
    tableName = "ffr_farms"
)
data class FfrFarmEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val ownership: String,
    @ColumnInfo(name = "image_urls")
    val imageUrls: List<String>? = null,
    @ColumnInfo(name = "plot_id")
    val plotId: String? = null,
    val pendingAction: PendingAction? = null,
    val location: LatLng? = null,
    val coordinates: List<LatLng>? = null,
    val area: Double,
    val measuredArea: Double? = null,
    val measuredType: String? = null,
    val openingSeasonId: String? = null
) {
    companion object {
        fun from(request: SaveFarmRequest, pendingAction: PendingAction) =
            FfrFarmEntity(
                id = generateIdIfRequired(request),
                name = request.name,
                ownership = request.ownership.asString(),
                imageUrls = mapImageUri(request.imageUri),
                plotId = request.plotId,
                pendingAction = pendingAction,
                location = request.measurement.location,
                coordinates = request.measurement.coordinates,
                area = request.measurement.area.value,
                measuredArea = request.measurement.measuredArea?.value,
                measuredType = request.measurement.measuredType.asStringOrNull()
            )

        private fun mapImageUri(imageUri: Uri?): List<String>? {
            if (imageUri == null) return null

            return listOf(imageUri.toString())
        }

        private fun generateIdIfRequired(request: SaveFarmRequest) =
            if (request.id.isNullOrEmpty()) UUID.randomUUID().toString() else request.id
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
        measuredType = AreaMeasureMethod.fromStringOrNull(measuredType)
    ),
    plotId = plotId,
    ownership = FarmOwnership.fromString(ownership),
    openingSeason = openingSeason?.asDomainModel()
)