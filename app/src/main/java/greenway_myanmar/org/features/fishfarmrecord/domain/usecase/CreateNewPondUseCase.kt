package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.domain.usecase.CoroutineUseCase
import com.greenwaymyanmar.core.domain.usecase.SimpleCoroutineUseCase
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.PondRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CreateNewPondUseCase.CreateNewPondRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CreateNewPondUseCase.CreateNewPondResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CreateNewPondUseCase @Inject constructor(
    private val pondRepository: PondRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : SimpleCoroutineUseCase<CreateNewPondRequest, CreateNewPondResult>(ioDispatcher) {

    override suspend fun execute(params: CreateNewPondRequest): Result<CreateNewPondResult> {
        return pondRepository.createPond(params.pondName)
    }

    data class CreateNewPondRequest(
        val pondName: String
    )

    data class CreateNewPondResult(
        val pondId: String
    )
}