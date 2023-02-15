package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncome
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveCropIncomeUseCase.SaveCropIncomeRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveCropIncomeUseCase.SaveCropIncomeResult
import kotlinx.coroutines.flow.Flow

interface CropIncomeRepository {
    suspend fun saveCropIncome(request: SaveCropIncomeRequest): SaveCropIncomeResult
    fun getCropIncomesStream(
        seasonId: String
    ): Flow<Result<List<CropIncome>>>
}
