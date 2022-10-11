/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greenway_myanmar.org.vo

import com.greenwaymyanmar.common.data.api.ApiErrorResponse
import com.greenwaymyanmar.common.data.api.ApiResponse
import greenway_myanmar.org.common.domain.entities.HttpStatus
import greenway_myanmar.org.common.domain.entities.ResourceError


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
  val message: String = error?.message().orEmpty()

  companion object {

    @JvmStatic
    fun <T> success(data: T?): Resource<T> {
      return success(data = data, code = HttpStatus.OK)
    }

    @JvmStatic
    fun <T> success(data: T?, code: HttpStatus = HttpStatus.OK): Resource<T> {
      return Resource(
        status = Status.SUCCESS, data = data, code = code, error = null,
      )
    }

    @JvmStatic
    fun <T> error(
      data: T? = null,
      code: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
      error: ResourceError
    ): Resource<T> {
      return Resource(Status.ERROR, data, code, error)
    }

    @JvmStatic
    fun <T> loading(data: T? = null): Resource<T> {
      return Resource(Status.LOADING, data, HttpStatus.PROCESSING)
    }

    @JvmStatic
    fun <T> from(e: Throwable): Resource<T> {
      return from(e, null)
    }

    @JvmStatic
    fun <T> from(e: Throwable, data: T? = null): Resource<T> {
      val response = ApiResponse.create<T>(e)
      return error(
        error = response.errorResource,
        data = data,
        code = HttpStatus.resolve(response.code) ?: HttpStatus.SERVICE_UNAVAILABLE,
      )
    }

    @JvmStatic
    fun <I, O> errorWithDefaultValue(
      apiErrorResponse: ApiErrorResponse<I>,
      defaultValue: O?
    ): Resource<O> {
      return error(
        defaultValue,
        HttpStatus.resolveOrDefault(apiErrorResponse.code),
        ResourceError.from(apiErrorResponse.error),
      )
    }

    @JvmStatic
    fun <I, O> errorWithNullValue(
      apiErrorResponse: ApiErrorResponse<I>
    ): Resource<O> {
      return error(
        null,
        HttpStatus.resolveOrDefault(apiErrorResponse.code),
        ResourceError.from(apiErrorResponse.error),
      )
    }

    @JvmStatic
    fun <I, O> errorWithNullValue(
      resource: Resource<I>
    ): Resource<O> {
      return error(
        null,
        resource.code,
        resource.error!!,
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
    return status == Status.SUCCESS
  }

  fun isError(): Boolean {
    return status == Status.ERROR
  }

  fun isLoading(): Boolean {
    return status == Status.LOADING
  }
}

fun <I, O> Resource<I>.map(mapper: (input: I?) -> O?): Resource<O> {
  return when (status) {
    Status.LOADING -> {
      Resource.loading(mapper(data))
    }
    Status.SUCCESS -> {
      Resource.success(mapper(data))
    }
    Status.ERROR -> {
      Resource.error(mapper(data), code, error!!)
    }
  }
}

//class Resource<T>(val status: Status, val data: T?, val message: String?, val code: Int) {
//    val error: ResourceError?
//    override fun equals(o: Any?): Boolean {
//        if (this === o) {
//            return true
//        }
//        if (o == null || javaClass != o.javaClass) {
//            return false
//        }
//        val resource = o as Resource<*>
//        if (status != resource.status) {
//            return false
//        }
//        return if (message != resource.message) {
//            false
//        } else data == resource.data
//    }
//
//    override fun hashCode(): Int {
//        var result = status.hashCode()
//        result = 31 * result + (message?.hashCode() ?: 0)
//        result = 31 * result + (data?.hashCode() ?: 0)
//        return result
//    }
//
//    override fun toString(): String {
//        return "Resource{" +
//                "status=" + status +
//                ", message='" + message + '\'' +
//                ", data=" + data +
//                '}'
//    }
//
//    val isLoading: Boolean
//        get() = status == Status.LOADING
//    val isSuccess: Boolean
//        get() = status == Status.SUCCESS
//
//    fun isError(): Boolean {
//        return status == Status.ERROR
//    }
//
//    companion object {
//        fun <T> success(data: T?): Resource<T?> {
//            return Resource(Status.SUCCESS, data, null, 200)
//        }
//
//        fun <T> error(msg: String?, data: T?, code: Int): Resource<T?> {
//            return Resource(Status.ERROR, data, msg, code)
//        }
//
//        fun <T> error(msg: String?): Resource<T?> {
//            return Resource(Status.ERROR, null, msg, 500)
//        }
//
//        fun <T> loading(data: T?): Resource<T?> {
//            return Resource(Status.LOADING, data, null, -1)
//        }
//
//        fun <T> from(e: Throwable?, data: T?): Resource<T?> {
//            val (error, code) = create<T>(e)
//            return error<T?>(
//                error,
//                data,
//                code
//            )
//        }
//
//        fun <T> from(e: Throwable?): Resource<T?> {
//            return from<T?>(e, null)
//        }
//    }
//
//    init {
//        error = IoMessageError(StringText(message ?: ""))
//    }
//}
