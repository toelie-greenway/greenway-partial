package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Crop
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiCrop(
    val id: String,
    val name: String,
    val iconImageUrl: String
): Parcelable

fun UiCrop.asDomainModel() = Crop(
    id = id,
    name = name,
    iconImageUrl = iconImageUrl
)