package com.greenwaymyanmar.core.presentation.model

import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.data.api.v3.NetworkDomainException
import greenway_myanmar.org.common.domain.entities.Text

sealed interface LoadingState<out T> {
    object Idle : LoadingState<Nothing>
    data class Success<T>(val data: T) : LoadingState<T>
    object Loading : LoadingState<Nothing>
    data class Empty(val message: Text? = null) : LoadingState<Nothing>
    data class Error(
        val exception: Throwable? = null,
        val message: Text? = exception.errorText(),
        val retryable: Boolean = exception is NetworkDomainException
    ) :
        LoadingState<Nothing>
}

fun <T> LoadingState<T>.isSuccess() = this is LoadingState.Success
fun <T> LoadingState<T>.isLoading() = this is LoadingState.Loading
fun <T> LoadingState<T>.isNotLoading() = !isLoading()
fun <T> LoadingState<T>.getDataOrNull() = (this as? LoadingState.Success)?.data
