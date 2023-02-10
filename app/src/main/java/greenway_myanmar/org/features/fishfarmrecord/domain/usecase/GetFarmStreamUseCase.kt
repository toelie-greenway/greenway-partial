package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmStreamUseCase @Inject constructor(
    private val farmRepository: FarmRepository,
) {
    operator fun invoke(request: GetFarmRequest): Flow<Result<Farm?>> {
        return farmRepository.getFarmStream(request.farmId, true)
    }

    data class GetFarmRequest(
        val farmId: String
    )
}