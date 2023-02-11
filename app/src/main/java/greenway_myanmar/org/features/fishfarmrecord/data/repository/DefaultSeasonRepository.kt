package greenway_myanmar.org.features.fishfarmrecord.data.repository

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class DefaultSeasonRepository @Inject constructor(
    private val seasonDao: FfrSeasonDao,
    private val farmDao: FfrFarmDao,
    private val network: FishFarmRecordNetworkDataSource,
    private val userHelper: UserHelper
) : SeasonRepository {

    override fun getClosedSeasonsStream(farmId: String): Flow<List<Season>> {
        return seasonDao.getSeasonsStream().map {
            it.map(FfrSeasonEntity::asDomainModel)
        }
    }

    override suspend fun saveSeason(request: SaveSeasonUseCase.SaveSeasonRequest): SaveSeasonUseCase.SaveSeasonResult {
        val response = network.postSeason(
            userId = userHelper.activeUserId.toString(),
            farmId = request.farmId,
            request = NetworkSeasonRequest.fromDomainRequest(request)
        )
        val entity = response.asEntity()
        seasonDao.upsertSeason(entity)
        farmDao.updateSeasonId(request.farmId, response.id)
        return SaveSeasonUseCase.SaveSeasonResult(entity.id)
    }

    override suspend fun endSeason(reason: SeasonEndReason) {
        delay(1500)
        Timber.d("Ending season ...")
    }

}