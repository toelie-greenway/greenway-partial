package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatus
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetQrOrderStatusUseCase @Inject constructor(
) {

    operator fun invoke(): GetQrOrderStatusUseCaseResult {
        return GetQrOrderStatusUseCaseResult.Success(
            listOf(
                QrOrderStatus(1, "ပို့ဆောင်ပြီး", null, Instant.now().minus(1, ChronoUnit.DAYS)),
                QrOrderStatus(2, "ဖုန်းဆက်အတည်ပြုပြီး", "အခုရေ ၅၀၀, ၂၀၀၀၀ ကျပ်", Instant.now().minus(2, ChronoUnit.DAYS)),
                QrOrderStatus(3, "GW မှအမှာစာအား စတင်ဆောင်ရွက်", null, Instant.now().plus(1, ChronoUnit.DAYS)),
                QrOrderStatus(4, "QR Code အခုရေ ၅၀၀ အမှာတင်သည်", null, Instant.now()),
            )
        )
    }

    sealed class GetQrOrderStatusUseCaseResult {
        object Loading : GetQrOrderStatusUseCaseResult()
        data class Success(val data: List<QrOrderStatus>) : GetQrOrderStatusUseCaseResult()
        data class Error(val message: String) : GetQrOrderStatusUseCaseResult()
    }
}