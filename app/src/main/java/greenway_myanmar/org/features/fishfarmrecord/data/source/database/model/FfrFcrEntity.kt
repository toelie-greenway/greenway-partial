package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fcr
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordRequest
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "ffr_fcr",
    primaryKeys = ["record_id", "fish_id"],
    foreignKeys = [
        ForeignKey(
            entity = FfrFcrRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["record_id"],
        )
    ],
    indices = [
        Index(value = ["record_id"]),
    ]
)
data class FfrFcrEntity(
    @Embedded(prefix = "fish_")
    val fish: FfrFishEntity,
    @ColumnInfo("feed_weight")
    val feedWeight: BigDecimal,
    @ColumnInfo("gain_weight")
    val gainWeight: BigDecimal,
    @ColumnInfo("record_id")
    val recordId: String
) {
    companion object {
        fun from(request: SaveFcrRecordRequest, recordId: String): List<FfrFcrEntity> {
            return request.ratios.map { ratio ->
                FfrFcrEntity(
                    fish = FfrFishEntity.fromDomainModel(ratio.fish),
                    feedWeight = ratio.feedWeight,
                    gainWeight = ratio.gainWeight,
                    recordId = recordId
                )
            }
        }
    }
}

fun FfrFcrEntity.asDomainModel() = Fcr(
    fish = fish.asDomainModel(),
    feedWeight = feedWeight,
    gainWeight = gainWeight
)