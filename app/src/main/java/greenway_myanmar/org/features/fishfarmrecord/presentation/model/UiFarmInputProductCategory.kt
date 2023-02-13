package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiFarmInputProductCategory(
    val id: String,
    val name: String,
    val type: UiFarmInputProductCategoryType,
    val isFingerling: Boolean
): Parcelable {
    companion object {
        fun fromDomainModel(domainModel: FarmInputProductCategory) = UiFarmInputProductCategory(
            id = domainModel.id,
            name = domainModel.name,
            type = UiFarmInputProductCategoryType.fromDomainModel(domainModel.type),
            isFingerling = domainModel.isFingerling
        )
    }
}