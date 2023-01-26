package com.greenwaymyanmar.core.presentation.model

import greenway_myanmar.org.common.domain.entities.Text

sealed interface LoadingState<out T> {
    object Idle : LoadingState<Nothing>
    data class Success<T>(val data: T) : LoadingState<T>
    object Loading : LoadingState<Nothing>
    data class Empty(val message: Text? = null) : LoadingState<Nothing>
    data class Error(val message: Text? = null, val retryable: Boolean = true) :
        LoadingState<Nothing>
}