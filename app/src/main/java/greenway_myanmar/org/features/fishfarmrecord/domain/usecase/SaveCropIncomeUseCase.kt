package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Crop
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.CropIncomeRepository
import kotlinx.datetime.Instant
import java.math.BigDecimal
import javax.inject.Inject

class SaveCropIncomeUseCase @Inject constructor(
    private val repository: CropIncomeRepository
) {
    suspend operator fun invoke(request: SaveCropIncomeRequest): SaveCropIncomeResult {
        return repository.saveCropIncome(request)
    }

    data class SaveCropIncomeRequest(
        val seasonId: String,
        val date: Instant,
        val crop: Crop,
        val price: BigDecimal
    )

    data class SaveCropIncomeResult(
        val id: String
    )
}