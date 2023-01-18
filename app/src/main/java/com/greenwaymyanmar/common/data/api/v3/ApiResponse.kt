package com.greenwaymyanmar.common.data.api.v3

import com.greenwaymyanmar.core.domain.model.exceptions.DomainException
import com.greenwaymyanmar.utils.errorMessage
import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object </T>
 */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<out T> {
    companion object {
        @JvmStatic
        fun <T> create(error: Throwable): ApiResponse<T> {
            return ApiExceptionResponse(error)
        }

        @JvmStatic
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse
                } else {
                    ApiSuccessResponse(body = body)
                }
            } else {
                ApiErrorResponse(
                    error = response.errorMessage(),
                    code = response.code()
                )
            }
        }
    }
}

/** separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null. */
object ApiEmptyResponse : ApiResponse<Nothing>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse(val error: String, val code: Int) : ApiResponse<Nothing>()

data class ApiExceptionResponse(val exception: Throwable) : ApiResponse<Nothing>()

fun <I, O> ApiResponse<I>.getDataOrThrow(mapper: (data: I) -> O): O {
    return when (this) {
        ApiEmptyResponse -> {
            throw UnknownDomainException("No Data!")
        }
        is ApiErrorResponse -> {
            throw UnknownDomainException(this.error)
        }
        is ApiExceptionResponse -> {
            throw NetworkDomainException(this.exception)
        }
        is ApiSuccessResponse -> {
            mapper(this.body)
        }
    }
}

fun <T> ApiResponse<T>.getDataOrThrow(): T {
    return when (this) {
        ApiEmptyResponse -> {
            throw UnknownDomainException("No Data!")
        }
        is ApiErrorResponse -> {
            throw UnknownDomainException(this.error)
        }
        is ApiExceptionResponse -> {
            throw NetworkDomainException(this.exception)
        }
        is ApiSuccessResponse -> {
            this.body
        }
    }
}

fun <T> ApiResponse<T>.isSuccessful(): Boolean {
    return when (this) {
        ApiEmptyResponse -> {
            true
        }
        is ApiErrorResponse -> {
            false
        }
        is ApiExceptionResponse -> {
            false
        }
        is ApiSuccessResponse -> {
            true
        }
    }
}

class UnknownDomainException(override val message: String) : DomainException(message)
class NetworkDomainException(val t: Throwable) : DomainException(t)