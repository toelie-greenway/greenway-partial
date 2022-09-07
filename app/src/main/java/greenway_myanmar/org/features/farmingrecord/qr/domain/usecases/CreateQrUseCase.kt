package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import javax.inject.Inject

class CreateQrUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {
    suspend operator fun invoke(params: Param): CreateQrResult {
        return qrRepository.createQr(
            farmId = params.farmId,
            seasonId = params.seasonId,
            farmLocationType = params.farmLocationType,
            optInShowPhone = params.optInShowPhone,
            optInShowFarmInput = params.optInShowFarmInput,
            optionShowYield = params.optionShowYield
        )
    }

    data class Param(
        val farmId: String,
        val seasonId: String,
        val farmLocationType: FarmLocationType,
        val optInShowPhone: Boolean,
        val optInShowFarmInput: Boolean,
        val optionShowYield: Boolean
    )

    sealed class CreateQrResult {
        data class Success(val qrId: String) : CreateQrResult()
        data class Error(val error: ResourceError) : CreateQrResult()
    }
}