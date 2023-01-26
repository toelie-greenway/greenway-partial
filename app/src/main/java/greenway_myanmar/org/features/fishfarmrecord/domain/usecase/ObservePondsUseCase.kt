package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.domain.usecase.FlowUseCase
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.PondRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePondsUseCase @Inject constructor(
    private val pondRepository: PondRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Farm>>(ioDispatcher) {
    override fun execute(params: Unit): Flow<Result<List<Farm>>> {
        return pondRepository.observePonds()
    }
}