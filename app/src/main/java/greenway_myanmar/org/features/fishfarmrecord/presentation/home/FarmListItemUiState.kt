package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import com.greenwaymyanmar.core.presentation.model.UiArea
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season

data class FarmListItemUiState(
    val id: String,
    val name: String,
    val images: List<String>,
    val area: UiArea?,
    val openingSeason: Season?,
    val pendingAction: PendingAction?
) {

    val thumbnailImageUrl: String? = images.firstOrNull()
    val hasOngoingSeason = openingSeason != null

    companion object {
        val Empty = FarmListItemUiState(
            id = "",
            name = "",
            area = null,
            images = emptyList(),
            openingSeason = null,
            pendingAction = null
        )

        fun fromDomain(farm: Farm) = FarmListItemUiState(
            id = farm.id,
            name = farm.name,
            images = farm.images.orEmpty(),
            area = farm.measurement.area.let { UiArea.fromDomain(it) },
            openingSeason = farm.openingSeason,
            pendingAction = farm.pendingAction
        )
    }
}