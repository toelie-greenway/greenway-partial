package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmsStreamUseCase @Inject constructor(
    private val farmRepository: FarmRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<List<Farm>> {
        return farmRepository.getFarmsStream()
    }
}