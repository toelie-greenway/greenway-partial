package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season

data class FarmListItemUiState(
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
        val Empty = FarmListItemUiState(
            id = "",
            name = "",
            area = null,
            images = emptyList(),
            ongoingSeason = null,
            contractFarmingCompany = null
        )

        fun fromDomain(farm: Farm) = FarmListItemUiState(
            id = farm.id,
            name = farm.name,
            images = farm.images,
            area = farm.area?.let { UiArea.fromDomain(it) },
            ongoingSeason = farm.ongoingSeason,
            contractFarmingCompany = farm.ongoingSeason?.contractFarmingCompany
        )
    }
}