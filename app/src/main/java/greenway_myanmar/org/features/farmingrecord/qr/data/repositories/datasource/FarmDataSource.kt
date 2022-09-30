package greenway_myanmar.org.features.farmingrecord.qr.data.repositories.datasource

import com.google.gson.Gson
import com.greenwaymyanmar.repository.datasource.BasePageKeyedGreenWayDataSource
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.api.Pagination
import greenway_myanmar.org.features.farmingrecord.qr.data.api.QrService
import greenway_myanmar.org.features.farmingrecord.qr.data.api.model.ApiFarm
import greenway_myanmar.org.features.farmingrecord.qr.data.api.model.ApiFarmList
import retrofit2.Call

class FarmDataSource
internal constructor(
    private val webservice: QrService,
    appExecutors: AppExecutors,
    gson: Gson,
    private val userId: Int
) : BasePageKeyedGreenWayDataSource<ApiFarmList, ApiFarm>(appExecutors, gson) {
    override fun createCall(): Call<ApiFarmList> {
        return webservice.getHarvestedFarmList(1, userId)
    }

    override fun createNextPageCall(nextPage: Int): Call<ApiFarmList> {
        return webservice.getHarvestedFarmList(nextPage, userId)
    }

    override fun getItemsFromResponse(listResponse: ApiFarmList): List<ApiFarm> {
        return listResponse.farms
    }

    override fun getPaginationFromResponseBody(
        listResponse: ApiFarmList
    ): Pagination {
        return listResponse.pagination
    }
}
