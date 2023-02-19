package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Crop
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiCrop(
    val id: String,
    val name: String,
    val iconImageUrl: String
) : Parcelable {
    companion object {
        fun fromDomainModel(domainModel: Crop) = UiCrop(
            id = domainModel.id,
            name = domainModel.name,
            iconImageUrl = domainModel.iconImageUrl
        )
    }
}

fun UiCrop.asDomainModel() = Crop(
    id = id,
    name = name,
    iconImageUrl = iconImageUrl
)