package greenway_myanmar.org.repository.tag

import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.Pagination
import com.greenwaymyanmar.common.data.api.v1.response.PostListResponse
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.repository.paging.datasource.BasePageKeyedManualGreenWayDataSource
import greenway_myanmar.org.vo.Post
import retrofit2.Call

class PageKeyedPostByTagDataSource constructor(
    private val webservice: GreenWayWebservice,
    appExecutors: AppExecutors,
    gson: Gson,
    private val networkPageSize: Int
) : BasePageKeyedManualGreenWayDataSource<PostListResponse, Post>(
    appExecutors, gson
) {
    override fun createCall(): Call<PostListResponse> {
        return webservice.getPosts(
            page = 1,
            limit = networkPageSize
        )
    }

    override fun createNextPageCall(nextPage: Int): Call<PostListResponse> {
        return webservice.getPosts(
            page = nextPage,
            limit = networkPageSize
        )
    }

    override fun getItemsFromResponse(requestType: PostListResponse?): List<Post> {
        return requestType?.posts.orEmpty()
    }

    override fun getPaginationFromResponseBody(requestType: PostListResponse?): Pagination? {
        return requestType?.pagination
    }
}