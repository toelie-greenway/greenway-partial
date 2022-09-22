package greenway_myanmar.org.features.farmingrecord.qr.domain.repositories

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrOrderUseCase.CreateQrOrderResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateUpdateQrUseCase.CreateUpdateQrResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrDetailUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase.GetQrOrderListResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrQuantityListUseCase
import java.time.Instant

interface QrRepository {
    suspend fun createUpdateQr(
        qrId: String?,
        farmId: String,
        seasonId: String,
        farmLocationType: FarmLocationType,
        optInShowPhone: Boolean,
        optInShowFarmInput: Boolean,
        optionShowYield: Boolean,
        qrLifetime: Instant,
        phone: String
    ): CreateUpdateQrResult

    suspend fun createQrOrder(
        qrId: String,
        quantity: Int
    ): CreateQrOrderResult

    suspend fun getQrOrders(): GetQrOrderListResult

    suspend fun getQrOrder(orderId: String): GetQrOrderUseCase.GetQrOrderResult

    suspend fun getQuantities(): GetQrQuantityListUseCase.GetQrQuantityListResult

    suspend fun getQrDetail(qrId: String): GetQrDetailUseCase.GetQrDetailResult
}