package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase
import kotlinx.coroutines.flow.Flow

interface FarmRepository {
    suspend fun postFarm(farmId: String): String
    fun getFarmsStream(): Flow<List<Farm>>
    fun getFarmMeasurementStream(farmId: String): Flow<FarmMeasurement>
    suspend fun saveFarm(request: SaveFarmUseCase.SaveFarmRequest): SaveFarmUseCase.SaveFarmResult
    fun observePond(pondId: String): Flow<Result<Farm>>
}