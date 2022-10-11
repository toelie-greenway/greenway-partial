package greenway_myanmar.org.features.farmingrecord.qr.data.repositories

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.*
import javax.inject.Inject

class DefaultQrRepository @Inject constructor(): QrRepository{
    override suspend fun createUpdateQr(
        qrId: String?,
        farmId: String,
        seasonId: String,
        farmLocationType: FarmLocationType,
        optInShowPhone: Boolean,
        optInShowFarmInput: Boolean,
        optionShowYield: Boolean,
        qrLifetime: Int,
        phone: String
    ): CreateUpdateQrUseCase.CreateUpdateQrResult {
        TODO("Not yet implemented")
    }

    override suspend fun createQrOrder(
        qrId: String,
        quantity: Int,
        qrUrl: String,
        qrIdNumber: String
    ): CreateQrOrderUseCase.CreateQrOrderResult {
        TODO("Not yet implemented")
    }

    override suspend fun getQrOrders(): GetQrOrderListUseCase.GetQrOrderListResult {
        TODO("Not yet implemented")
    }

    override suspend fun getQrOrder(orderId: String): GetQrOrderUseCase.GetQrOrderResult {
        TODO("Not yet implemented")
    }

    override suspend fun getQuantities(): GetQrQuantityListUseCase.GetQrQuantityListResult {
        TODO("Not yet implemented")
    }

    override suspend fun getQrLifetimes(): GetQrLifetimeListUseCase.GetQrLifetimeListResult {
        TODO("Not yet implemented")
    }

    override suspend fun getQrDetail(qrId: String): GetQrDetailUseCase.GetQrDetailResult {
        TODO("Not yet implemented")
    }

}
