package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import kotlinx.serialization.Serializable

@Entity(
    tableName = "ffr_fishes"
)
@Serializable
data class FfrFishEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val species: String? = null,
    @ColumnInfo(name = "icon_image_url")
    val iconImageUrl: String? = null
) {
    companion object {
        fun fromDomainModel(domainModel: Fish) = FfrFishEntity(
            id = domainModel.id,
            name = domainModel.name,
            species = domainModel.species,
            iconImageUrl = domainModel.iconImageUrl
        )
    }
}

fun FfrFishEntity.asDomainModel() = Fish(
    id = id,
    name = name,
    iconImageUrl = iconImageUrl.orEmpty(),
    species = species.orEmpty()
)