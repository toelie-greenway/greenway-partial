package com.greenwaymyanmar.common.data.repository.util.v1

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.greenwaymyanmar.common.data.api.ApiEmptyResponse
import com.greenwaymyanmar.common.data.api.ApiErrorResponse
import com.greenwaymyanmar.common.data.api.ApiResponse
import com.greenwaymyanmar.common.data.api.ApiSuccessResponse
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.common.domain.entities.HttpStatus
import greenway_myanmar.org.util.SingleLiveEvent
import greenway_myanmar.org.vo.Resource

/**
 * A generic class that can provide a resource backed by the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 *
 * @param <ParamType>    Type for the Parameter
 * @param <ResponseType> Type for the API response.
</ResponseType></ParamType> */
abstract class NetworkTask<ParamType, ResponseType> @MainThread constructor(private val appExecutors: AppExecutors) {
  private val result = MediatorLiveData<Resource<ResponseType>>()
  private fun fetchFromNetwork() {
    val paramSource: LiveData<ParamType>? = onPreExecute()
    if (paramSource != null) {
      result.addSource(paramSource) { paramType: ParamType ->
        result.removeSource(paramSource)
        doNetworkOperation(paramType)
      }
    } else {
      doNetworkOperation(null)
    }
  }

  private fun doNetworkOperation(paramType: ParamType?) {
    val apiResponse = createCall(paramType)
    result.addSource(apiResponse) { response: ApiResponse<ResponseType> ->
      result.removeSource(apiResponse)
      when (response) {
        is ApiSuccessResponse -> {
          appExecutors.diskIO().execute {
            val requestType = processResponse(response)
            onPostExecute(requestType)
            appExecutors.mainThread().execute {
              setValue(Resource.success(requestType))
            }
          }
        }
        is ApiEmptyResponse -> {
          setValue(Resource.success(null))
        }
        is ApiErrorResponse -> {
          onFetchFailed()
          onFetchFailed(response.code)
          Resource.error(
            data = null,
            error = response.errorResource,
            code = HttpStatus.resolve(response.code) ?: HttpStatus.SERVICE_UNAVAILABLE,
          )
        }
      }
    }
  }

  @MainThread
  private fun setValue(newValue: Resource<ResponseType>) {
    if (result.value != newValue) {
      result.value = newValue
    }
  }

  protected open fun onFetchFailed(responseCode: Int) {}
  protected open fun onFetchFailed() {}

  @MainThread
  protected open fun onPreExecute(): SingleLiveEvent<ParamType>? {
    // no-op; client will implement if required
    return null
  }

  @WorkerThread
  protected open fun onPostExecute(item: ResponseType) {
    // no-op; client will implement if required
  }

  fun asLiveData(): LiveData<Resource<ResponseType>> {
    return result
  }

  @WorkerThread
  protected fun processResponse(response: ApiSuccessResponse<ResponseType>): ResponseType {
    return response.body
  }

  @MainThread
  protected abstract fun createCall(params: ParamType?): LiveData<ApiResponse<ResponseType>>

  init {
    result.value = Resource.loading(null)
    fetchFromNetwork()
  }
}
