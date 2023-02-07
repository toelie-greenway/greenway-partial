package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmMeasurementUseCase @Inject constructor(
    private val farmRepository: FarmRepository
) {
    operator fun invoke(request: GetFarmMeasurementRequest): Flow<FarmMeasurement> {
        return farmRepository.getFarmMeasurementStream(
            farmId = request.farmId
        )
    }

    data class GetFarmMeasurementRequest(
        val farmId: String
    )
}
