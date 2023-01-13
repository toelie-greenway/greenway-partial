package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.domain.usecase.FlowUseCase
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Pond
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.PondRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePondUseCase @Inject constructor(
    private val pondRepository: PondRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<ObservePondUseCase.ObservePondRequest, Pond>(ioDispatcher) {
    override fun execute(params: ObservePondRequest): Flow<Result<Pond>> {
        return pondRepository.observePond(params.pondId)
    }

    data class ObservePondRequest(
        val pondId: String
    )
}