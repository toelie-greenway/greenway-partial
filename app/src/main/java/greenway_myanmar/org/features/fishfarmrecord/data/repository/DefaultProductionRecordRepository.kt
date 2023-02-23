package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrProductionRecordDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainMode
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkProductionRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecord
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ProductionRecordRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultProductionRecordRepository @Inject constructor(
    private val userHelper: UserHelper,
    private val network: FishFarmRecordNetworkDataSource,
    private val productionRecordDao: FfrProductionRecordDao
): ProductionRecordRepository {

    override suspend fun saveProductionRecord(request: SaveProductionRecordRequest): SaveProductionRecordResult {
        val response = network.postProductionRecord(
            userId = userHelper.activeUserId.toString(),
            request = NetworkProductionRecordRequest.fromDomainRequest(request)
        )
        val entity = response.asEntity()
        productionRecordDao.upsertProductionRecord(entity)
        return SaveProductionRecordResult(entity.id)
    }

    override fun getProductionsStream(seasonId: String): Flow<Result<List<ProductionRecord>>> {
        return networkBoundResult(
            query = {
                productionRecordDao.getProductionRecordsStream(seasonId).map { list ->
                    list.map {
                        it.asDomainMode()
                    }
                }
            },
            fetch = {
                network.getProductionRecords(
                    userId = userHelper.activeUserId.toString(),
                    seasonId = seasonId
                )
            },
            saveFetchResult = { response ->
                productionRecordDao.upsertProductionRecords(
                    response.map { it.asEntity() }
                )
            },
            onFetchFailed = {

            },
            shouldFetch = {
                true
            }
        )
    }
}