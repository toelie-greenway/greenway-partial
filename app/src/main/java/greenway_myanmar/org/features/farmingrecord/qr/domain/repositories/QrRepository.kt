package greenway_myanmar.org.features.farmingrecord.qr.domain.repositories

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrOrderUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrOrderUseCase.CreateQrOrderResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrUseCase.CreateQrResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase.GetQrOrderListResult

interface QrRepository {
    suspend fun createQr(
        farmId: String,
        seasonId: String,
        farmLocationType: FarmLocationType,
        optInShowPhone: Boolean,
        optInShowFarmInput: Boolean,
        optionShowYield: Boolean
    ): CreateQrResult

    suspend fun createQrOrder(
        qrId: String,
        quantity: Int
    ): CreateQrOrderResult

    suspend fun getQrOrders(): GetQrOrderListResult
}