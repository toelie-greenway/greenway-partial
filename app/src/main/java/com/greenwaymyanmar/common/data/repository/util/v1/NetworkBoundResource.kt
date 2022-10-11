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
import greenway_myanmar.org.util.Objects
import greenway_myanmar.org.vo.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 *
 * @param <ResultType>  Type for the Resource data.
 * @param <RequestType> Type for the API response.
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor(private val appExecutors: AppExecutors) {
  private val result = MediatorLiveData<Resource<ResultType>>()

  @MainThread
  private fun setValue(newValue: Resource<ResultType>) {
    if (!Objects.equals(result.value, newValue)) {
      result.value = newValue
    }
  }

  private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
    val apiResponse = createCall()
    // we re-attach dbSource as a new source, it will dispatch its latest value quickly
    result.addSource(dbSource) { newData: ResultType -> setValue(Resource.loading(newData)) }
    result.addSource(apiResponse) { response: ApiResponse<RequestType> ->
      result.removeSource(apiResponse)
      result.removeSource(dbSource)
      when (response) {
        is ApiSuccessResponse -> {
          appExecutors.diskIO().execute {
            saveCallResult(processResponse(response))
            appExecutors.mainThread().execute { // we specially request a new live data,
              // otherwise we will get immediately last cached value,
              // which may not be updated with latest results received from network.
              result.addSource(
                loadFromDb(),
              ) { newData: ResultType -> setValue(Resource.success(newData)) }
            }
          }
        }
        is ApiEmptyResponse -> {
          appExecutors.mainThread().execute { setValue(Resource.success(null)) }
        }
        is ApiErrorResponse -> {
          onFetchFailed() // for backward compatibility
          onFetchFailed(response.code) // add error response code
          result.addSource(
            dbSource,
          ) { newData: ResultType ->
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
  }

  protected open fun onFetchFailed() {}

  protected open fun onFetchFailed(responseCode: Int) {}
  fun asLiveData(): LiveData<Resource<ResultType>> {
    return result
  }

  @WorkerThread
  protected fun processResponse(response: ApiSuccessResponse<RequestType>): RequestType {
    return response.body
  }

  protected open fun saveToDbFirstIfRequired() {
    // Required child will override
  }

  protected fun dispatchValueManuallyIfRequired() {
    // Required child will override
  }

  @WorkerThread
  protected abstract fun saveCallResult(item: RequestType)

  @MainThread
  protected abstract fun shouldFetch(data: ResultType?): Boolean

  @MainThread
  protected abstract fun loadFromDb(): LiveData<ResultType>

  @MainThread
  protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

  init {
    result.value = Resource.loading(null)
    appExecutors.diskIO().execute { saveToDbFirstIfRequired() }
    val dbSource = loadFromDb()
    result.addSource(dbSource) { data: ResultType ->
      result.removeSource(dbSource)
      if (shouldFetch(data)) {
        fetchFromNetwork(dbSource)
      } else {
        result.addSource(dbSource) { newData: ResultType ->
          setValue(
            Resource.success(
              newData,
            ),
          )
        }
      }
    }
  }
}
