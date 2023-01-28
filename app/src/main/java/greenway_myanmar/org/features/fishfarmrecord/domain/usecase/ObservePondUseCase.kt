package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.domain.usecase.FlowUseCase
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePondUseCase @Inject constructor(
    private val farmRepository: FarmRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<ObservePondUseCase.ObservePondRequest, Farm>(ioDispatcher) {
    override fun execute(params: ObservePondRequest): Flow<Result<Farm>> {
        return farmRepository.observePond(params.pondId)
    }

    data class ObservePondRequest(
        val pondId: String
    )
}