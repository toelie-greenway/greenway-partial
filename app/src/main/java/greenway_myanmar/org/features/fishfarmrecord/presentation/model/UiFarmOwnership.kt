package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmOwnership

enum class UiFarmOwnership {
    Own,
    Rent
}

fun UiFarmOwnership.asDomainModel() = when (this) {
    UiFarmOwnership.Own -> {
        FarmOwnership.OWN
    }
    UiFarmOwnership.Rent -> {
        FarmOwnership.RENT
    }
}