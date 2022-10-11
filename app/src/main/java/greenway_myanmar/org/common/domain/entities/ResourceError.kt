package greenway_myanmar.org.common.domain.entities

import android.content.Context
import com.greenwaymyanmar.common.data.api.errorMessage
import com.greenwaymyanmar.common.data.api.errorText
import greenway_myanmar.org.R
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

    fun message(): String {
        return error.string()
    }

    companion object {
        @JvmStatic
        fun from(message: String): ResourceError {
            return IoMessageError(Text.StringText(message))
        }

        @JvmStatic
        fun from(exception: Exception): ResourceError {
            return IoMessageError(exception.errorText())
        }

        @JvmStatic
        fun from(response: Response<*>): ResourceError {
            return IoMessageError(
                error = Text.StringText(response.errorMessage()),
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
