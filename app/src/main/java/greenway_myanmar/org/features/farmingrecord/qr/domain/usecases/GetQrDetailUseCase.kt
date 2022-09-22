package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrDetail
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import javax.inject.Inject

class GetQrDetailUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {
    suspend operator fun invoke(params: Param): GetQrDetailResult {
        return qrRepository.getQrDetail(params.qrId)
    }

    data class Param(val qrId: String)

    sealed class GetQrDetailResult {
        data class Success(val data: QrDetail) : GetQrDetailResult()
        data class Error(val error: ResourceError) : GetQrDetailResult()
    }
}