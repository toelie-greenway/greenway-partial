package greenway_myanmar.org.repository.tag

import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.Pagination
import com.greenwaymyanmar.common.data.api.v1.response.ProductListResponse
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.repository.paging.datasource.BasePageKeyedManualGreenWayDataSource
import greenway_myanmar.org.vo.Product
import retrofit2.Call

class PageKeyedProductByTagDataSource constructor(
    private val webservice: GreenWayWebservice,
    appExecutors: AppExecutors,
    gson: Gson,
    private val networkPageSize: Int
) : BasePageKeyedManualGreenWayDataSource<ProductListResponse, Product>(
    appExecutors, gson
) {
    override fun createCall(): Call<ProductListResponse> {
        return webservice.getMainProducts(
            query = "",
            tags = "1",
            page = 1,
            limit = networkPageSize
        )
    }

    override fun createNextPageCall(nextPage: Int): Call<ProductListResponse> {
        return webservice.getMainProducts(
            query = "",
            tags = "1",
            page = nextPage,
            limit = networkPageSize
        )
    }

    override fun getItemsFromResponse(requestType: ProductListResponse?): List<Product> {
        return requestType?.products.orEmpty()
    }

    override fun getPaginationFromResponseBody(requestType: ProductListResponse?): Pagination? {
        return requestType?.pagination
    }
}