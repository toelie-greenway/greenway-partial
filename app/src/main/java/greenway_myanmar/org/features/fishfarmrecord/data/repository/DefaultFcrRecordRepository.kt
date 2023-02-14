package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.db.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntities
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrRecordDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.PopulatedFcrRecord
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkFcrRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FcrRecordRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultFcrRecordRepository @Inject constructor(
    private val fcrRecordDao: FfrFcrRecordDao,
    private val fcrDao: FfrFcrDao,
    private val network: FishFarmRecordNetworkDataSource,
    private val userHelper: UserHelper
) : FcrRecordRepository {

    override suspend fun saveFcrRecord(request: SaveFcrRecordRequest): SaveFcrRecordResult {
        val response = network.postFcrRecord(
            userId = userHelper.activeUserId.toString(),
            request = NetworkFcrRecordRequest.fromDomainRequest(request)
        )
        val entity = response.asEntity(request.seasonId)
        fcrRecordDao.insertFcrRecord(entity)

        val ratios = response.record.orEmpty().asEntities(entity.id)
        fcrDao.upsertFcrEntities(ratios)
        return SaveFcrRecordResult(entity.id)
    }

    override fun getFcrRecordsStream(
        seasonId: String
    ): Flow<Result<List<FcrRecord>>> {
        return networkBoundResult(
            query = {
                fcrRecordDao.getFcrRecordsStream(seasonId).map { list ->
                    list.map(PopulatedFcrRecord::asDomainModel)
                }
            },
            fetch = {
                network.getFcrRecords(
                    userId = userHelper.activeUserId.toString(),
                    seasonId = seasonId
                )
            },
            saveFetchResult = { response ->
                fcrRecordDao.upsertFcrRecords(
                    response.map { it.asEntity(seasonId) }
                )
                response.forEach { record ->
                    fcrDao.upsertFcrEntities(
                        record.record.orEmpty().asEntities(record.id)
                    )
                }
            },
            onFetchFailed = {

            },
            shouldFetch = {
                true
            }
        )
    }
}