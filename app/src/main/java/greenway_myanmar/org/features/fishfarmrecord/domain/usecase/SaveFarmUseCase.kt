package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import android.net.Uri
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmOwnership
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import javax.inject.Inject

class SaveFarmUseCase @Inject constructor(
    private val farmRepository: FarmRepository
) {
    suspend operator fun invoke(request: SaveFarmRequest): SaveFarmResult {
        return farmRepository.saveFarm(request)
    }

    data class SaveFarmRequest(
        val id: String? = null,
        val name: String,
        val measurement: FarmMeasurement,
        val imageUri: Uri? = null,
        val ownership: FarmOwnership,
        val plotId: String? = null
    )

    data class SaveFarmResult(
        val id: String
    )
}