package greenway_myanmar.org.features.thread.presentation

import androidx.lifecycle.ViewModel
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategoryType
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow<ThreadUiState>(ThreadUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                votedTags = listOf(
                    UiVotableTag(
                        tag = UiTag(
                            id = "1",
                            name = "ပင်စည်ပုပ်ရောဂါ",
                            category = UiCategory(
                                id = "1",
                                name = "ပိုးမွှားရောဂါ",
                                imageUrl = "https://picsum.photos/id/12/960/540",
                                type = UiCategoryType.Agri
                            ),
                            imageUrls = listOf(
                                "https://picsum.photos/id/210/960/540"
                            )
                        ),
                        voteCount = 2,
                        isVoted = true,
                        suggestedCategory = null
                    )

                )
            )
        }
    }

}