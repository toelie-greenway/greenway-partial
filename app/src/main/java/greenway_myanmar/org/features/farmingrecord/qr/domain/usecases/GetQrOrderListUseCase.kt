package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetQrOrderListUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {

    suspend operator fun invoke(): GetQrOrderListResult {
        return qrRepository.getQrOrders()
    }

    sealed class GetQrOrderListResult {
        data class Success(val data: List<QrOrder>) : GetQrOrderListResult()
        data class Error(val error: ResourceError) : GetQrOrderListResult()
    }
}