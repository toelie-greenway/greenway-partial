package com.greenwaymyanmar.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { Result.Loading }
        .catch { Result.Error(it) }
}

fun <I, O> Flow<I>.asResult(mapper: (input: I) -> O): Flow<Result<O>> {
    return this
        .map<I, Result<O>> {
            Result.Success(mapper(it))
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

fun <I, O> Result<I>.map(mapper: (input: I) -> O): Result<O> {
    return when (this) {
        is Result.Success -> {
            Result.Success(mapper(this.data))
        }
        is Result.Error -> {
            Result.Error(this.exception)
        }
        Result.Loading -> {
            Result.Loading
        }
    }
}

fun <I, O> Result<List<I>>.mapList(mapper: (input: I) -> O): Result<List<O>> {
    return when (this) {
        is Result.Success -> {
            Result.Success(
                this.data.map {
                    mapper(it)
                }
            )
        }
        is Result.Error -> {
            Result.Error(this.exception)
        }
        Result.Loading -> {
            Result.Loading
        }
    }
}