package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import javax.inject.Inject

class EndSeasonUseCase @Inject constructor(
    private val seasonRepository: SeasonRepository
) {
    suspend operator fun invoke(request: EndSeasonRequest) {
        seasonRepository.endSeason(request.reason)
    }

    data class EndSeasonRequest(
        val reason: SeasonEndReason
    )
}