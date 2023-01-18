package com.greenwaymyanmar.common.data.api.util

import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = ApiResponseCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                "return type must be parameterized as Call<ApiResponse<<Foo>> or Call<ApiResponse<out Foo>>")
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        val rawResponseType = getRawType(responseType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (rawResponseType != ApiResponse::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        if (responseType !is ParameterizedType) {
            throw IllegalStateException(
                "Response must be parameterized as ApiResponse<Foo> or ApiResponse<out Foo>")
        }

        val successBodyType = getParameterUpperBound(0, responseType)

        return ApiResponseCallAdapter<Any>(successBodyType)
    }
}