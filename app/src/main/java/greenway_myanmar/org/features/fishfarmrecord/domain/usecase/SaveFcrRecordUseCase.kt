package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fcr
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FcrRecordRepository
import kotlinx.datetime.Instant
import javax.inject.Inject

class SaveFcrRecordUseCase @Inject constructor(
    private val fcrRecordRepository: FcrRecordRepository
) {
    suspend operator fun invoke(request: SaveFcrRecordRequest) : SaveFcrRecordResult {
        return fcrRecordRepository.saveFcrRecord(request)
    }

    data class SaveFcrRecordRequest(
        val id: String?,
        val date: Instant,
        val ratios: List<Fcr>
    )

    data class SaveFcrRecordResult(
        val id: String
    )
}