package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.db.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkSeasonRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveSeasonUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultSeasonRepository @Inject constructor(
    private val seasonDao: FfrSeasonDao,
    private val farmDao: FfrFarmDao,
    private val network: FishFarmRecordNetworkDataSource,
    private val userHelper: UserHelper
) : SeasonRepository {

    override fun getClosedSeasonsStream(farmId: String): Flow<Result<List<Season>>> {
        // TODO: Replace with paging
        return networkBoundResult(
            query = {
                seasonDao.getClosedSeasonsStream(farmId).map { list ->
                    list.map(FfrSeasonEntity::asDomainModel)
                }
            },
            fetch = {
                network.getClosedSeasons(
                    userId = userHelper.activeUserId.toString(),
                    farmId = farmId,
                    page = 1
                )
            },
            saveFetchResult = { response ->
                seasonDao.upsertSeasonEntities(response.data.orEmpty().map {
                    it.asEntity(farmId)
                })
            },
            onFetchFailed = {
                /* no-op */
            },
            shouldFetch = {
                true
            }
        )
    }

    override suspend fun saveSeason(request: SaveSeasonUseCase.SaveSeasonRequest): SaveSeasonUseCase.SaveSeasonResult {
        val response = network.postSeason(
            userId = userHelper.activeUserId.toString(),
            farmId = request.farmId,
            request = NetworkSeasonRequest.fromDomainRequest(request)
        )
        val entity = response.asEntity(request.farmId)
        seasonDao.upsertSeasonEntity(entity)
        farmDao.updateOpeningSeasonId(request.farmId, response.id)
        return SaveSeasonUseCase.SaveSeasonResult(entity.id)
    }

    override suspend fun endSeason(farmId: String, seasonId: String, reason: SeasonEndReason) {
        val networkSeason = network.patchSeason(
            farmId = farmId,
            seasonId = seasonId,
            userId = userHelper.activeUserId.toString(),
            fields = mapOf(
                "is_end" to true,
                "season_end_reason_id" to reason.id
            )
        )
        farmDao.updateOpeningSeasonId(farmId, null)
        seasonDao.upsertSeasonEntity(
            networkSeason.asEntity(farmId)
        )
    }
}