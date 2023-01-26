package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import com.greenwaymyanmar.core.presentation.model.UiUnitOfMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiFarmInputProduct(
    val id: String,
    val name: String,
    val thumbnail: String,
    val units: List<UiUnitOfMeasurement>
): Parcelable {
    companion object {
        fun fromDomainModel(domainModel: FarmInputProduct) = UiFarmInputProduct(
            id = domainModel.id,
            name = domainModel.name,
            thumbnail = domainModel.thumbnail,
            units = domainModel.units.map { UiUnitOfMeasurement.fromDomainModel(it) }
        )
    }
}