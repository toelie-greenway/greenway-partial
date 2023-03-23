package greenway_myanmar.org.features.tag.presentation

sealed class TagEvent {
    data class OnTabChanged(val tab: UiTagTab) : TagEvent()
}