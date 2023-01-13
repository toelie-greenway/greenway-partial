package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Pond
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season

data class PondListItemUiState(
    val id: String,
    val name: String,
    val images: List<String>,
    val area: UiArea?,
    val ongoingSeason: Season?,
    val contractFarmingCompany: ContractFarmingCompany?
) {

    val thumbnailImageUrl: String? = images.firstOrNull()
    val hasThumbnailImage = images.isNotEmpty()
    val hasOngoingSeason = ongoingSeason != null
    val hasContractFarmingCompany = contractFarmingCompany != null

    companion object {
        val Empty = PondListItemUiState(
            id = "",
            name = "",
            area = null,
            images = emptyList(),
            ongoingSeason = null,
            contractFarmingCompany = null
        )

        fun fromDomain(pond: Pond) = PondListItemUiState(
            id = pond.id,
            name = pond.name,
            images = pond.images,
            area = pond.area?.let { UiArea.fromDomain(it) },
            ongoingSeason = pond.ongoingSeason,
            contractFarmingCompany = pond.ongoingSeason?.contractFarmingCompany
        )
    }
}