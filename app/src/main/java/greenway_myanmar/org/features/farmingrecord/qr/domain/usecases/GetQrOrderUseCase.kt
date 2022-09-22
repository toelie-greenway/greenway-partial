package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import javax.inject.Inject

class GetQrOrderUseCase @Inject constructor(
    private val qrRepository: QrRepository
) {

    suspend operator fun invoke(params: Param): GetQrOrderResult {
        return qrRepository.getQrOrder(params.orderId)
    }

    //            listOf(
//                QrOrderStatusDetail(1, "ပို့ဆောင်ပြီး", null, Instant.now().minus(1, ChronoUnit.DAYS)),
//                QrOrderStatusDetail(2, "ဖုန်းဆက်အတည်ပြုပြီး", "အခုရေ ၅၀၀, ၂၀၀၀၀ ကျပ်", Instant.now().minus(2, ChronoUnit.DAYS)),
//                QrOrderStatusDetail(3, "GW မှအမှာစာအား စတင်ဆောင်ရွက်", null, Instant.now().plus(1, ChronoUnit.DAYS)),
//                QrOrderStatusDetail(4, "QR Code အခုရေ ၅၀၀ အမှာတင်သည်", null, Instant.now()),
//            )
//        )

    data class Param(val orderId: String)

    sealed class GetQrOrderResult {
        data class Success(val data: QrOrder) : GetQrOrderResult()
        data class Error(val error: ResourceError) : GetQrOrderResult()
    }
}