package greenway_myanmar.org.features.farmingrecord.qr.data.repositories

import androidx.lifecycle.Transformations.map
import com.greenwaymyanmar.api.ApiEmptyResponse
import com.greenwaymyanmar.api.ApiErrorResponse
import com.greenwaymyanmar.api.ApiResponse
import com.greenwaymyanmar.api.ApiSuccessResponse
import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.farmingrecord.qr.data.api.QrService
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateQrOrderPayload
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateQrPayload
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrOrderUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrOrderUseCase.CreateQrOrderResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrUseCase.CreateQrResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase.GetQrOrderListResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultQrRepository @Inject constructor(
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val qrService: QrService
) : QrRepository {

    override suspend fun createQr(
        farmId: String,
        seasonId: String,
        farmLocationType: FarmLocationType,
        optInShowPhone: Boolean,
        optInShowFarmInput: Boolean,
        optionShowYield: Boolean
    ): CreateQrResult {
        return withContext(ioDispatcher) {
            try {
                val payload = CreateQrPayload(
                    farmId = farmId,
                    seasonId = seasonId,
                    farmLocationType = farmLocationType.value,
                    optInShowPhone = optInShowPhone,
                    optInShowFarmInput = optInShowFarmInput,
                    optInShowYield = optionShowYield
                )
                val response = qrService.postQr(payload)
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        CreateQrResult.Success(
                            qrId = apiResponse.body.data.id
                        )
                    }
                    is ApiErrorResponse -> {
                        CreateQrResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        throw IllegalStateException("ApiResponse should not be empty!")
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                CreateQrResult.Error(ResourceError.from(e))
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
}