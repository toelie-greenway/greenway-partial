package com.greenwaymyanmar.common.feature.tag.presentation.tag

sealed class TagEvent {
    data class OnTabChanged(val tab: UiTagTab) : TagEvent()
}