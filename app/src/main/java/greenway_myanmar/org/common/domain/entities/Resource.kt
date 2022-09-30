package greenway_myanmar.org.common.domain.entities

import com.greenwaymyanmar.api.ApiResponse
import greenway_myanmar.org.vo.Status
import greenway_myanmar.org.vo.Status.SUCCESS
import greenway_myanmar.org.vo.Status.ERROR
import greenway_myanmar.org.vo.Status.LOADING

/**
 * A generic class that holds a value with its loading status.
 * @param <T> </T>
 */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val code: HttpStatus,
    val error: ResourceError? = null
) {
    companion object {
        fun <T> success(data: T?, code: HttpStatus = HttpStatus.OK): Resource<T> {
            return Resource(SUCCESS, data, code, null)
        }

        fun <T> error(
            data: T? = null,
            code: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
            error: ResourceError
        ): Resource<T> {
            return Resource(ERROR, data, code, error)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, HttpStatus.PROCESSING)
        }

        fun <T> from(e: Throwable, data: T? = null): Resource<T> {
            val response = ApiResponse.create<T>(e)
            return error(
                error = response.error,
                data = data,
                code = HttpStatus.resolve(response.code) ?: HttpStatus.SERVICE_UNAVAILABLE
            )
        }
    }

    fun isNullOrEmpty(): Boolean {
        return when (data) {
            null -> {
                true
            }
            is List<*> -> {
                data.isEmpty()
            }
            else -> {
                false
            }
        }
    }

    fun isSuccess(): Boolean {
        return status == SUCCESS
    }

    fun isError(): Boolean {
        return status == ERROR
    }

    fun isLoading(): Boolean {
        return status == LOADING
    }
}

fun <I, O> Resource<I>.map(mapper: (input: I?) -> O?): Resource<O> {
    return when (status) {
        LOADING -> {
            Resource.loading(mapper(data))
        }
        SUCCESS -> {
            Resource.success(mapper(data))
        }
        ERROR -> {
            Resource.error(mapper(data), code, error!!)
        }
    }
}