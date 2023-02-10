package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.db.UserHelper
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asNetworkRequestModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase.SaveFarmRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultFarmRepository @Inject constructor(
    private val farmDao: FfrFarmDao,
    private val seasonDao: FfrSeasonDao,
    private val userHelper: UserHelper,
    private val network: FishFarmRecordNetworkDataSource
) : FarmRepository {

    override fun getFarmsStream(): Flow<List<Farm>> {
        GlobalScope.launch(Dispatchers.IO) {
            val networkFarmList = network.getFarms(userHelper.activeUserId.toString())
            farmDao.upsertFarms(networkFarmList.data.orEmpty().map(NetworkFarm::asEntity))
        }
        return farmDao.getFarmsStream().map { list ->
            list.map { farm ->
                farm.asDomainModel()
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

    override suspend fun postFarm(farmId: String): String {
        val farm: FfrFarmEntity = farmDao.getFarmById(farmId)
        val response = network.postFarm(
            userId = userHelper.activeUserId.toString(),
            request = farm.asNetworkRequestModel()
        )
        farmDao.deleteFarmById(farmId)
        farmDao.upsertFarm(response.asEntity())
        return response.id
    }

    override fun getFarmMeasurementStream(farmId: String): Flow<FarmMeasurement> {
        return farmDao.getFarmStreamById(farmId).map { farm ->
            FarmMeasurement(
                location = farm.location,
                coordinates = farm.coordinates,
                area = Area.acre(farm.area),
                measuredArea = if (farm.measuredArea != null) Area.acre(farm.measuredArea) else null,
                measuredType = AreaMeasureMethod.fromStringOrNull(farm.measuredType),
                depth = farm.depth
            )
        }
    }

    override fun observePond(pondId: String): Flow<Result<Farm>> {
        return flow {
            // TODO: Implement
        }
    }
}