package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import androidx.annotation.StringRes
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FishSize

enum class UiFishSize(@StringRes val labelResId: Int) {
    Large(R.string.ffr_production_label_fish_size_large),
    Medium(R.string.ffr_production_label_fish_size_medium),
    Small(R.string.ffr_production_label_fish_size_small),
    HomePresent(R.string.ffr_production_label_fish_size_home_present);

    companion object {
        fun fromDomainModel(domainModel: FishSize) = when (domainModel) {
            FishSize.Large -> Large
            FishSize.Medium -> Medium
            FishSize.Small -> Small
            FishSize.HomePresent -> HomePresent
        }
    }
}

fun UiFishSize.asDomainModel() = when (this) {
    UiFishSize.Large -> {
        FishSize.Large
    }
    UiFishSize.Medium -> {
        FishSize.Medium
    }
    UiFishSize.Small -> {
        FishSize.Small
    }
    UiFishSize.HomePresent -> {
        FishSize.HomePresent
    }
}