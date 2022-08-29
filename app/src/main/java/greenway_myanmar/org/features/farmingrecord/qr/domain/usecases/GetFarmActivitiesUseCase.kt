package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrFarmActivity
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetFarmActivitiesUseCase @Inject constructor(

) {
    suspend operator fun invoke(): GetFarmActivitiesResult {
        return GetFarmActivitiesResult.Success(
            listOf(
                QrFarmActivity(
                    "မြေပြုပြင်ခြင်း", Instant.now(), "နွားချေး - (၂) လှည်း\n" +
                            "စီပီ ၁၅း၁၅း၁၅ - (၂) အိတ်"
                ),
                QrFarmActivity("ပျိုးထောင်ခြင်း", Instant.now().minus(1, ChronoUnit.DAYS)),
                QrFarmActivity("စိုက်ပျိုးခြင်း", Instant.now().minus(7, ChronoUnit.DAYS)),
                QrFarmActivity("အပင်ပြုစုခြင်း", Instant.now().minus(8, ChronoUnit.DAYS)),
                QrFarmActivity("မြေသြဇာကျွေးခြင်း", Instant.now().minus(7, ChronoUnit.DAYS)),
                QrFarmActivity("ပိုးမွှားရောဂါ၊ ဖျက်ပိုးနှင့် ပေါင်း", Instant.now().minus(6, ChronoUnit.DAYS)),
                QrFarmActivity("ပိုးမွှားရောဂါ၊ ဖျက်ပိုးနှင့် ပေါင်း", Instant.now().minus(5, ChronoUnit.DAYS)),
                QrFarmActivity("ရိတ်သိမ်းခြင်း", Instant.now().minus(12, ChronoUnit.DAYS)),
                QrFarmActivity("ရိတ်သိမ်းခြင်း", Instant.now().minus(2, ChronoUnit.DAYS))
            )
        )
    }

    sealed class GetFarmActivitiesResult {
        data class Success(val data: List<QrFarmActivity>) : GetFarmActivitiesResult()
        data class Error(val message: String) : GetFarmActivitiesResult()
    }
}