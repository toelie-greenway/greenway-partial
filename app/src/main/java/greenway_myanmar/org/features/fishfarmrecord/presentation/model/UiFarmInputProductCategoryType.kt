package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategoryType

enum class UiFarmInputProductCategoryType(val key: String) {
    Aquaculture("aquaculture"),
    Default("default");

    companion object {
        fun fromDomainModel(domainModel: FarmInputProductCategoryType) = when (domainModel) {
            FarmInputProductCategoryType.Aquaculture -> Aquaculture
            else -> Default
        }
    }
}