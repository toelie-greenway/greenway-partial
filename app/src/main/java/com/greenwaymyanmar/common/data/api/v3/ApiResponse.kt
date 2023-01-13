package com.greenwaymyanmar.common.data.api.v3

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
        fun <T> create(error: Throwable): ApiExceptionResponse {
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