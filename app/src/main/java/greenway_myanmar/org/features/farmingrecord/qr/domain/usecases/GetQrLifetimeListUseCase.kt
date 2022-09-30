package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrLifetime
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrQuantity
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import javax.inject.Inject

class GetQrLifetimeListUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {
    suspend operator fun invoke(): GetQrLifetimeListResult {
        return qrRepository.getQrLifetimes()
    }

    sealed class GetQrLifetimeListResult {
        data class Success(val lifetimes: List<QrLifetime>) : GetQrLifetimeListResult()
        data class Error(val error: ResourceError) : GetQrLifetimeListResult()
    }
}