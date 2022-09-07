package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import androidx.annotation.StringRes
import greenway_myanmar.org.R
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType

enum class UiFarmLocationType(val value: String, @StringRes val label: Int) {
    Map(FarmLocationType.Map.value, R.string.label_farming_record_qr_farm_location_map) {
        override fun toDomain() = FarmLocationType.Map
    },
    Village(
        FarmLocationType.Village.value,
        R.string.label_farming_record_qr_farm_location_village
    ) {
        override fun toDomain() = FarmLocationType.Village
    },
    Township(
        FarmLocationType.Township.value,
        R.string.label_farming_record_qr_farm_location_township
    ) {
        override fun toDomain() = FarmLocationType.Township
    };

    abstract fun toDomain(): FarmLocationType

    companion object {
        fun fromDomain(domainModel: FarmLocationType): UiFarmLocationType {
            return when (domainModel) {
                FarmLocationType.Map -> Map
                FarmLocationType.Township -> Township
                FarmLocationType.Village -> Village
            }
        }
    }
}