package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FcrRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFcrRecordsStreamUseCase @Inject constructor(
    private val fcrRecordRepository: FcrRecordRepository
) {
    operator fun invoke(request: GetFcrRecordsRequest): Flow<Result<List<FcrRecord>>> {
        return fcrRecordRepository.getFcrRecordsStream(request.seasonId)
    }

    data class GetFcrRecordsRequest(
        val seasonId: String
    )
}