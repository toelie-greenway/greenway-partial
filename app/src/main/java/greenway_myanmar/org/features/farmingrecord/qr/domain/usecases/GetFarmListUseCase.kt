package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFarmListUseCase @Inject constructor(

) {

   suspend operator fun invoke(): GetFarmListResult {
       delay(700)
//       return GetFarmListResult.Error("Error!")
       return GetFarmListResult.Success(
           listOf(
               Farm("1", "A"),
               Farm("2", "B"),
               Farm("3", "C"),
               Farm("4", "D"),
               Farm("5", "E"),
               Farm("6", "F"),
               Farm("7", "G"),
               Farm("8", "H"),
           )
       )
    }

    sealed class GetFarmListResult {
        data class Success(val data: List<Farm>) : GetFarmListResult()
        data class Error(val message: String) : GetFarmListResult()
    }
}