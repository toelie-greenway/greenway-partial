package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
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
    operator fun invoke(): Flow<Result<List<Farm>>> {
        return farmRepository.getFarmsStream()
    }
}