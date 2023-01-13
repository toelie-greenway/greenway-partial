package com.greenwaymyanmar.core.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.greenwaymyanmar.common.result.Result

abstract class SimpleCoroutineUseCase<in P, out R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(params: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(params)
            }
        } catch (ignore: CancellationException) {
            throw ignore
        }
    }

    protected abstract suspend fun execute(params: P): Result<R>
}