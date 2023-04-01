package greenway_myanmar.org.repository.tag

import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.response.PostListResponse
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.repository.paging.datasource.BaseDataSourceManualFactory
import greenway_myanmar.org.repository.paging.datasource.BasePageKeyedManualGreenWayDataSource
import greenway_myanmar.org.vo.Post

class PostByTagDataSourceFactory(
    private val webservice: GreenWayWebservice,
    private val appExecutors: AppExecutors,
    private val gson: Gson,
    private val networkPageSize: Int
) : BaseDataSourceManualFactory<PostListResponse, Post>() {

    override fun createDataSource(): BasePageKeyedManualGreenWayDataSource<PostListResponse, Post> {
        return PageKeyedPostByTagDataSource(
            webservice, appExecutors, gson, networkPageSize
        )
    }
}
