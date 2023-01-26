package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CreateNewPondUseCase
import kotlinx.coroutines.flow.Flow

interface PondRepository {
    fun observePonds(): Flow<Result<List<Farm>>>
    fun observePond(pondId: String): Flow<Result<Farm>>
    suspend fun createPond(
        pondName: String
    ): Result<CreateNewPondUseCase.CreateNewPondResult>
}