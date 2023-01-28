package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase
import kotlinx.coroutines.flow.Flow

interface FarmRepository {
    fun getFarmsStream(): Flow<List<Farm>>
    suspend fun saveFarm(request: SaveFarmUseCase.SaveFarmRequest): SaveFarmUseCase.SaveFarmResult
    fun observePond(pondId: String): Flow<Result<Farm>>
}