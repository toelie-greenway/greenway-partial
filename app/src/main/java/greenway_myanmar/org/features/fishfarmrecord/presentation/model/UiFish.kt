package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiFish(
    val id: String,
    val name: String,
    val imageUrl: String,
    val species: String
) : Parcelable {
    companion object {
        fun fromDomain(domainModel: Fish) = UiFish(
            id = domainModel.id,
            name = domainModel.name,
            imageUrl = domainModel.imageUrl,
            species = ""
        )
    }
}
