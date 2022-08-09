package com.greenwaymyanmar.utils

import kotlinx.coroutines.CancellationException

inline fun <R> runCancellableCatching(block: () -> R): Result<R> {
  return try {
    Result.success(block())
  } catch (e: CancellationException) {
    throw e
  } catch (e: Throwable) {
    Result.failure(e)
  }
}
