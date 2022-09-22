package greenway_myanmar.org.features.farmingrecord.qr.data.repositories

import com.greenwaymyanmar.api.ApiEmptyResponse
import com.greenwaymyanmar.api.ApiErrorResponse
import com.greenwaymyanmar.api.ApiResponse
import com.greenwaymyanmar.api.ApiSuccessResponse
import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.farmingrecord.qr.data.api.QrService
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateQrOrderPayload
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateUpdateQrPayload
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.*
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrOrderUseCase.CreateQrOrderResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateUpdateQrUseCase.CreateUpdateQrResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrDetailUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase.GetQrOrderListResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderUseCase.GetQrOrderResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrQuantityListUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultQrRepository @Inject constructor(
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val qrService: QrService
) : QrRepository {

    override suspend fun createUpdateQr(
        qrId: String?,
        farmId: String,
        seasonId: String,
        farmLocationType: FarmLocationType,
        optInShowPhone: Boolean,
        optInShowFarmInput: Boolean,
        optionShowYield: Boolean,
        qrLifetime: Instant,
        phone: String
    ): CreateUpdateQrResult {
        return withContext(ioDispatcher) {
            try {
                val payload = CreateUpdateQrPayload(
                    farmId = farmId,
                    seasonId = seasonId,
                    farmLocationType = farmLocationType.value,
                    optInShowPhone = optInShowPhone,
                    optInShowFarmInput = optInShowFarmInput,
                    optInShowYield = optionShowYield,
                    phone = phone,
                    qrLifetime = DateTimeFormatter.ISO_LOCAL_DATE
                        .withLocale(Locale.US)
                        .withZone(ZoneId.of("UTC"))
                        .format(qrLifetime)
                )
                val response = if (!qrId.isNullOrEmpty()) {
                    qrService.updateQr(qrId, payload)
                } else {
                    qrService.postQr(payload)
                }
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        CreateUpdateQrResult.Success(
                            qrId = apiResponse.body.data.qrId
                        )
                    }
                    is ApiErrorResponse -> {
                        CreateUpdateQrResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        throw IllegalStateException("ApiResponse should not be empty!")
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                CreateUpdateQrResult.Error(ResourceError.from(e))
            }
        }
    }

    override suspend fun createQrOrder(
        qrId: String,
        quantity: Int
    ): CreateQrOrderResult {
        return withContext(ioDispatcher) {
            try {
                val payload = CreateQrOrderPayload(
                    qrId = qrId,
                    quantity = quantity
                )
                val response = qrService.postQrOrder(payload)
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        CreateQrOrderResult.Success
                    }
                    is ApiErrorResponse -> {
                        CreateQrOrderResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        CreateQrOrderResult.Success
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                CreateQrOrderResult.Error(ResourceError.from(e))
            }
        }
    }

    override suspend fun getQrOrders(): GetQrOrderListResult {
        return withContext(ioDispatcher) {
            try {
                val response = qrService.getQrOrders()
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        GetQrOrderListResult.Success(apiResponse.body.data.map {
                            it.toDomain()
                        })
                    }
                    is ApiErrorResponse -> {
                        GetQrOrderListResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        GetQrOrderListResult.Success(emptyList())
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                GetQrOrderListResult.Error(ResourceError.from(e))
            }
        }
    }

    override suspend fun getQrOrder(orderId: String): GetQrOrderResult {
        return withContext(ioDispatcher) {
            try {
                val response = qrService.getOrder(orderId)
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        GetQrOrderResult.Success(apiResponse.body.data.toDomain())
                    }
                    is ApiErrorResponse -> {
                        GetQrOrderResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        GetQrOrderResult.Error(ResourceError.from(response))
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                GetQrOrderResult.Error(ResourceError.from(e))
            }
        }
    }

    override suspend fun getQuantities(): GetQrQuantityListUseCase.GetQrQuantityListResult {
        return withContext(ioDispatcher) {
            try {
                val response = qrService.getQuantities()
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        GetQrQuantityListUseCase.GetQrQuantityListResult.Success(apiResponse.body.data.map { it.toDomain() })
                    }
                    is ApiErrorResponse -> {
                        GetQrQuantityListUseCase.GetQrQuantityListResult.Error(
                            ResourceError.from(
                                response
                            )
                        )
                    }
                    is ApiEmptyResponse -> {
                        GetQrQuantityListUseCase.GetQrQuantityListResult.Error(
                            ResourceError.from(
                                response
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                GetQrQuantityListUseCase.GetQrQuantityListResult.Error(ResourceError.from(e))
            }
        }
    }

    override suspend fun getQrDetail(qrId: String): GetQrDetailUseCase.GetQrDetailResult {

        val activities = listOf(
            QrFarmActivity(
                "မြေပြုပြင်ခြင်း", Instant.now(), "နွားချေး - (၂) လှည်း\n" +
                        "စီပီ ၁၅း၁၅း၁၅ - (၂) အိတ်"
            ),
            QrFarmActivity("ပျိုးထောင်ခြင်း", Instant.now().minus(1, ChronoUnit.DAYS)),
            QrFarmActivity("စိုက်ပျိုးခြင်း", Instant.now().minus(7, ChronoUnit.DAYS)),
            QrFarmActivity("အပင်ပြုစုခြင်း", Instant.now().minus(8, ChronoUnit.DAYS)),
            QrFarmActivity("မြေသြဇာကျွေးခြင်း", Instant.now().minus(7, ChronoUnit.DAYS)),
            QrFarmActivity(
                "ပိုးမွှားရောဂါ၊ ဖျက်ပိုးနှင့် ပေါင်း",
                Instant.now().minus(6, ChronoUnit.DAYS)
            ),
            QrFarmActivity(
                "ပိုးမွှားရောဂါ၊ ဖျက်ပိုးနှင့် ပေါင်း",
                Instant.now().minus(5, ChronoUnit.DAYS)
            ),
            QrFarmActivity("ရိတ်သိမ်းခြင်း", Instant.now().minus(12, ChronoUnit.DAYS)),
            QrFarmActivity("ရိတ်သိမ်းခြင်း", Instant.now().minus(2, ChronoUnit.DAYS))
        )

        return GetQrDetailUseCase.GetQrDetailResult.Success(
            data = QrDetail(
                id = "1",
                qrUrl = "https://greenwaymyanmar.com/",
                user = QrUser(
                    id = "107",
                    avatar = "https://picsum.photos/500/500",
                    name = "မောင်တိုးလိုင်း",
                    userInfo = QrUser.QrUserInfo(
                        career = "တောင်သူလယ်သမား"
                    )
                ),
                farm = Farm(
                    id = "1",
                    name = "စမ်းသပ်စိုက်ခင်း"
                ),
                season = Season(
                    id = "1",
                    seasonName = "ဆောင်းရာသီ",
                    crop = Crop(
                        id = "1",
                        title = "ပိန်းဥ"
                    )
                ),
                farmActivities = activities
            )
        )

    }
}