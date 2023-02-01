package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase.SaveFarmRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultFarmRepository @Inject constructor(
    private val farmDao: FfrFarmDao,
    private val seasonDao: FfrSeasonDao,
) : FarmRepository {

    override fun getFarmsStream(): Flow<List<Farm>> {
        return farmDao.getFarmsStream().map { list ->
            list.map { farm ->
                if (!farm.openingSeasonId.isNullOrEmpty()) {
                    val season = seasonDao.getSeasonById(farm.openingSeasonId)
                    if (season != null) {
                        farm.asDomainModel(season)
                    } else {
                        farm.asDomainModel()
                    }
                } else {
                    farm.asDomainModel()
                }
            }
        }
    }

    override suspend fun saveFarm(request: SaveFarmRequest): SaveFarmUseCase.SaveFarmResult {
        val action = if (request.id.isNullOrEmpty()) {
            PendingAction.CREATE
        } else {
            PendingAction.UPDATE
        }
        val entity = FfrFarmEntity.from(request, action)
        farmDao.upsertFarm(entity)
        return SaveFarmUseCase.SaveFarmResult(entity.id)
    }

    override fun observePond(pondId: String): Flow<Result<Farm>> {
        TODO("Not yet implemented")
    }
}