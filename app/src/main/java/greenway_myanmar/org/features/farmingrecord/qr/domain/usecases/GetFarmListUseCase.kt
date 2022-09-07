package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.FarmRepository
import javax.inject.Inject

class GetFarmListUseCase @Inject constructor(
    private val farmRepository: FarmRepository
) {

    suspend operator fun invoke(): GetFarmListResult {
        return farmRepository.getFarmList()
    }

    sealed class GetFarmListResult {
        data class Success(val data: List<Farm>) : GetFarmListResult()
        data class Error(val error: ResourceError) : GetFarmListResult()
    }
}