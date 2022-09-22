package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import java.time.Instant
import javax.inject.Inject

class CreateUpdateQrUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {
    suspend operator fun invoke(params: Param): CreateUpdateQrResult {
        return qrRepository.createUpdateQr(
            qrId = params.qrId,
            farmId = params.farmId,
            seasonId = params.seasonId,
            farmLocationType = params.farmLocationType,
            optInShowPhone = params.optInShowPhone,
            optInShowFarmInput = params.optInShowFarmInput,
            optionShowYield = params.optionShowYield,
            qrLifetime = params.qrLifetime,
            phone = params.phone
        )
    }

    data class Param(
        val qrId: String?,
        val farmId: String,
        val seasonId: String,
        val farmLocationType: FarmLocationType,
        val optInShowPhone: Boolean,
        val optInShowFarmInput: Boolean,
        val optionShowYield: Boolean,
        val qrLifetime: Instant,
        val phone: String
    )

    sealed class CreateUpdateQrResult {
        data class Success(val qrId: String) : CreateUpdateQrResult()
        data class Error(val error: ResourceError) : CreateUpdateQrResult()
    }
}