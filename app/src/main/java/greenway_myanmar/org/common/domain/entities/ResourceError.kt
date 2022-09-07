package greenway_myanmar.org.common.domain.entities

import android.content.Context
import com.greenwaymyanmar.api.ApiErrorResponse
import com.greenwaymyanmar.api.ApiResponse
import com.greenwaymyanmar.api.errorMessage
import com.greenwaymyanmar.api.errorText
import greenway_myanmar.org.R
import greenway_myanmar.org.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.Response

sealed class ResourceError(open val error: Text) {

    data class IoMessageError(override val error: Text) :
        ResourceError(error)

    data class IoBodyError(
        override val error: Text = Text.StringText("Oops!"),
        val body: String
    ) :
        ResourceError(error)

    data class NoInternetConnectionError(
        override val error: Text = Text.ResourceText(R.string.error_no_network)
    ) : ResourceError(error)

    fun asString(context: Context): String {
        return error.asString(context)
    }

    companion object {
        fun from(exception: Exception): ResourceError {
            return IoMessageError(exception.errorText())
        }

        fun from(response: Response<*>): ResourceError {
            return IoMessageError(
                error = Text.StringText(response.errorMessage())
            )
        }
    }
}

//fun <I, O> Flow<I>.asResource(mapper: (input: I?) -> O?): Flow<Resource<O>> {
//    return this.map<I, Resource<O>> { Resource.success(mapper(it)) }
//        .onStart { emit(Resource.loading(null)) }
//        .catch {
//            val error = ApiResponse.create<Any?>(it)
//            emit(Resource.error(error.errorMessage, null, 500))
//        }
//}
