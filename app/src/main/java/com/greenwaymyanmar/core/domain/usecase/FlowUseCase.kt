package com.greenwaymyanmar.core.domain.usecase

import com.greenwaymyanmar.common.result.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

abstract class FlowUseCase<in P, out R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    operator fun invoke(params: P): Flow<Result<R>> = execute(params)
        .onStart { emit(Result.Loading) }
        .catch { e ->
            if (e is CancellationException) {
                throw e
            } else {
                emit(Result.Error(e))
            }
        }.flowOn(coroutineDispatcher)

    protected abstract fun execute(params: P): Flow<Result<R>>
}
