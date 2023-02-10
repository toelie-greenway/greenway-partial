package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FishRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFishesStreamUseCase @Inject constructor(
    private val fishRepository: FishRepository
) {

    operator fun invoke(query: String): Flow<Result<List<Fish>>> {
        return fishRepository.getFishesStream(query)
    }
}