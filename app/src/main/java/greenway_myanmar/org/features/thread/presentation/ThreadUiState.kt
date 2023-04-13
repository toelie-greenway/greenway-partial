package greenway_myanmar.org.features.thread.presentation

import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategoryType
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.getTagColor


data class ThreadUiState(
    val category: UiCategory? = UiCategory(
        id = "1",
        name = "စမ်းသပ်အကြောင်းအရာ",
        type = UiCategoryType.Livestock,
        imageUrl = ""
    ),
    val categoryType: UiCategoryType = UiCategoryType.Livestock,
    val cropOrAnimalName: String = "နာနတ်",
    val tagVoteOptions: List<UiVotableTag> = emptyList(),
    val approvedTags: List<UiTag> = listOf(
        UiTag(
            id = "1",
            name = "ရော်ဘာဈေးကွက်သတင်း",
            category = UiCategory(
                id = "1",
                name = "စမ်းသပ်အကြောင်းအရာ",
                imageUrl = "",
                type = UiCategoryType.Livestock
            ),
            categories = null,
            imageUrls = listOf(
                "https://extension.umn.edu/sites/extension.umn.edu/files/marssonia-leaf-spot-on-euonymus-grabowski.jpg"
            ),
            color = getTagColor(0)
        ),
        UiTag(
            id = "2",
            name = "စိုက်ပျိုးရေ",
            category = UiCategory(
                id = "1",
                name = "စမ်းသပ်အကြောင်းအရာ",
                imageUrl = "",
                type = UiCategoryType.Livestock
            ),
            categories = null,
            imageUrls = listOf(
                "https://extension.umn.edu/sites/extension.umn.edu/files/marssonia-leaf-spot-on-euonymus-grabowski.jpg"
            ),
            color = getTagColor(1)
        )
    ),
    val isTechnician: Boolean = false,
) {
    val hasApprovedTag: Boolean = approvedTags.isNotEmpty()

}