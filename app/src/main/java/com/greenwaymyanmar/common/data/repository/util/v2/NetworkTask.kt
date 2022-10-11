package com.greenwaymyanmar.common.data.repository.util.v2

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.greenwaymyanmar.common.data.api.ApiEmptyResponse
import com.greenwaymyanmar.common.data.api.ApiErrorResponse
import com.greenwaymyanmar.common.data.api.ApiResponse
import com.greenwaymyanmar.common.data.api.ApiSuccessResponse
import com.greenwaymyanmar.common.data.api.v2.response.AsylSuccessResponse
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.common.domain.entities.HttpStatus
import greenway_myanmar.org.util.Objects
import greenway_myanmar.org.util.SingleLiveEvent
import greenway_myanmar.org.vo.Resource

/**
 * A generic class that can provide a resource backed by the network.
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 *
 * @param <ParamType> Type for the Parameter
 * @param <ResponseType> Type for the API response. </ResponseType></ParamType>
 */
abstract class NetworkTask<ParamType, ResponseType>
@MainThread
constructor(private val appExecutors: AppExecutors) {
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
    result.addSource(apiResponse) { response ->
      result.removeSource(apiResponse)

      when (response) {
        is ApiSuccessResponse -> {
          appExecutors.diskIO().execute {
            val requestType = processResponse(response)
            onPostExecute(requestType)
            onPostExecute(requestType, paramType)
            appExecutors.mainThread().execute { setValue(Resource.success(requestType)) }
          }
        }
        is ApiEmptyResponse -> {
          appExecutors.mainThread().execute { setValue(Resource.success(null)) }
        }
        is ApiErrorResponse -> {
          onFetchFailed()
          onFetchFailed(response.code)
          setValue(
            Resource.error(
              data = null,
              error = response.errorResource,
              code = HttpStatus.resolve(response.code) ?: HttpStatus.SERVICE_UNAVAILABLE,
            ),
          )
        }
      }
    }
  }

  @MainThread
  private fun setValue(newValue: Resource<ResponseType>) {
    if (!Objects.equals(result.value, newValue)) {
      result.value = newValue
    }
  }

  protected fun onFetchFailed(responseCode: Int) {}
  protected fun onFetchFailed() {}

  @MainThread
  open fun onPreExecute(): SingleLiveEvent<ParamType>? {
    // no-op; client will implement if required
    return null
  }

  @WorkerThread
  open fun onPostExecute(item: ResponseType) {
    // no-op; client will implement if required
  }

  @WorkerThread
  open fun onPostExecute(item: ResponseType, params: ParamType?) {
    // no-op; client will implement if required
  }

  fun asLiveData(): LiveData<Resource<ResponseType>> {
    return result
  }

  @WorkerThread
  open fun processResponse(
    response: ApiSuccessResponse<AsylSuccessResponse<ResponseType>>
  ): ResponseType {
    return response.body.data
  }

  @MainThread
  protected abstract fun createCall(
    params: ParamType?
  ): LiveData<ApiResponse<AsylSuccessResponse<ResponseType>>>

  init {
    result.value = Resource.loading(null)
    fetchFromNetwork()
  }
}
