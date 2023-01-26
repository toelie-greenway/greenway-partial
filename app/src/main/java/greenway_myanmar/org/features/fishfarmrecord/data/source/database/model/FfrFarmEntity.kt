package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.LatLngConverter

@Entity(
    tableName = "ffr_farms"
)
@TypeConverters(
    LatLngConverter::class
)
data class FfrFarmEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val ownership: String,
    @ColumnInfo(name = "image_urls")
    val imageUrls: List<String>,
    @ColumnInfo(name = "plot_id")
    val plotId: String,
    val location: LatLng? = null
)

data class FarmAreaEntity(
    private val location: LatLng? = null,
    private val acre: Double,
    private val measurementType: String? = null,
    private val measuredAcre: Double? = null,
    private val depth: Double? = null,
    private val measurement: List<LatLng>? = null
)