package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Loan
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import kotlinx.datetime.Instant
import javax.inject.Inject

class SaveSeasonUseCase @Inject constructor(
    private val seasonRepository: SeasonRepository
) {
    suspend operator fun invoke(request: SaveSeasonRequest): SaveSeasonResult {
        return seasonRepository.saveSeason(request)
    }

    data class SaveSeasonRequest(
        val id: String? = null,
        val farmMeasurement: FarmMeasurement,
        val seasonName: String,
        val seasonStartDate: Instant,
        val fishes: List<Fish>,
        val company: ContractFarmingCompany? = null,
        val loan: Loan? = null,
   )

    data class SaveSeasonResult(
        val id: String
    )
}