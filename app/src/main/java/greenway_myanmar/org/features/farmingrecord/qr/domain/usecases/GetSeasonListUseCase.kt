package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSeasonListUseCase @Inject constructor(

) {

    suspend operator fun invoke(params: Param): GetSeasonListResult {
        delay(700)
//       return GetFarmListResult.Error("Error!")
        return GetSeasonListResult.Success(
            listOf(
                Season("1", "A"),
                Season("2", "B"),
                Season("3", "C"),
                Season("4", "D"),
                Season("5", "E"),
                Season("6", "F"),
                Season("7", "G"),
                Season("8", "H"),
            )
        )
    }

    data class Param(val farmId: String)

    sealed class GetSeasonListResult {
        data class Success(val data: List<Season>) : GetSeasonListResult()
        data class Error(val message: String) : GetSeasonListResult()
    }
}