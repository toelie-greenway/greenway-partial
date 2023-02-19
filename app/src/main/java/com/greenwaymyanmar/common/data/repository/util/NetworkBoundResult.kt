package com.greenwaymyanmar.common.data.repository.util

import com.greenwaymyanmar.common.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

// credit: https://stackoverflow.com/a/58583081
inline fun <ResultType, RequestType> networkBoundResult(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { },
    crossinline shouldFetch: suspend (ResultType) -> Boolean = { true }
) =
    flow {
        emit(Result.Loading)
        //delay(1500)
        val data = query().first()

        val flow =
            if (shouldFetch(data)) {
                try {
                    saveFetchResult(fetch())
                    query().map { Result.Success(it) }
                } catch (throwable: Throwable) {
                    onFetchFailed(throwable)
                    query().map { Result.Error(throwable) }
                }
            } else {
                query().map { Result.Success(it) }
            }

        emitAll(flow)
    }


inline fun <T> networkResult(
    crossinline fetch: suspend () -> T,
) =
    flow {
        emit(Result.Loading)
        try {
            val result = fetch()
            emit(Result.Success(result))
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            emit(Result.Error(throwable))
        }
    }