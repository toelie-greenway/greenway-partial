package greenway_myanmar.org.features.thread.presentation

import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import greenway_myanmar.org.vo.Category


data class ThreadUiState(
    val category: Category? = Category().apply {
        id = "1"
        title = "စမ်းသပ်အကြောင်းအရာ"
        type = Category.TYPE_AGRI
        image = ""
    },
    val cropOrAnimalName: String = "နာနတ်",
    val tagVoteOptions: List<UiVotableTag> = emptyList(),
    val approvedTags: List<UiTag> = emptyList(),
    val isTechnician: Boolean = false,
) {
    val hasApprovedTag: Boolean = approvedTags.isNotEmpty()
}