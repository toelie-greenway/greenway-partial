package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrQuantity
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import javax.inject.Inject

class GetQrQuantityListUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {
    suspend operator fun invoke() : GetQrQuantityListResult {
        return qrRepository.getQuantities()
    }

    sealed class GetQrQuantityListResult {
        data class Success(val prices: List<QrQuantity>) : GetQrQuantityListResult()
        data class Error(val error: ResourceError) : GetQrQuantityListResult()
    }
}