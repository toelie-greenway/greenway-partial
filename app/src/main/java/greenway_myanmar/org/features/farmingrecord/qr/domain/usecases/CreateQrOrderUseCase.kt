package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import javax.inject.Inject

class CreateQrOrderUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {
    suspend operator fun invoke(params: Param): CreateQrOrderResult {
        return qrRepository.createQrOrder(
            qrId = params.qrId,
            quantity = params.quantity,
            qrUrl = params.qrUrl,
            qrIdNumber = params.qrIdNumber
        )
    }

    data class Param(
        val qrId: String,
        val quantity: Int,
        val qrUrl: String,
        val qrIdNumber: String
    )

    sealed class CreateQrOrderResult {
        object Success : CreateQrOrderResult()
        data class Error(val error: ResourceError) : CreateQrOrderResult()
    }
}