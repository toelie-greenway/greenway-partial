package greenway_myanmar.org.features.tag.presentation

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.tag.presentation.model.UiTag
import greenway_myanmar.org.features.tag.presentation.model.UiTagPost
import greenway_myanmar.org.features.tag.presentation.model.UiTagProduct
import greenway_myanmar.org.features.tag.presentation.model.UiTagThread

data class TagUiState(
    val tag: LoadingState<UiTag> = LoadingState.Idle,
    val tab: UiTagTab = UiTagTab.Thread,
    val threads: LoadingState<List<UiTagThread>> = LoadingState.Idle,
    val posts: LoadingState<List<UiTagPost>> = LoadingState.Idle,
    val hasMoreThread: Boolean = false,
    val hasMorePost: Boolean = false,
    val products: LoadingState<List<UiTagProduct>> = LoadingState.Idle
) {
    companion object {
        val Empty = TagUiState()
    }
}

enum class UiTagTab(val position: Int) {
    Thread(0),
    Post(1);

    companion object {
        fun fromPosition(position: Int): UiTagTab {
            return when(position) {
                Thread.position -> Thread
                Post.position -> Post
                else -> throw IllegalArgumentException("Invalid position $position")
            }
        }
    }
}