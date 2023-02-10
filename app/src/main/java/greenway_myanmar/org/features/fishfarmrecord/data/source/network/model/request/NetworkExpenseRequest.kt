package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveExpenseUseCase.SaveExpenseRequest
import greenway_myanmar.org.util.toServerDateString
import kotlinx.serialization.Serializable

@Serializable
data class NetworkExpenseRequest(
    val season_id: String,
    val expense_category_id: String,
    val date: String,
    val labour_qty: Int? = null,
    val labour_cost: Double? = null,
    val family_qty: Int? = null,
    val family_cost: Double? = null,
    val machinery_cost: Double? = null,
    val photos: List<String>? = null,
    val remark: String? = null,
    val inputs: List<NetworkFarmInputRequest>? = null
) {
    companion object {
        fun fromDomainRequest(request: SaveExpenseRequest) = NetworkExpenseRequest(
            season_id = request.seasonId,
            expense_category_id = request.expenseCategory.id,
            date = request.date.toServerDateString(),
            labour_qty = request.labourQuantity,
            labour_cost = request.labourCost?.toDouble(),
            family_qty = request.familyQuantity,
            family_cost = request.familyCost?.toDouble(),
            machinery_cost = request.machineryCost?.toDouble(),
            photos = emptyList(), //TODO:
            remark = request.remark,
            inputs = request.inputs?.map {
                NetworkFarmInputRequest.fromDomainModel(it)
            }
        )
    }
}

@Serializable
data class NetworkFarmInputRequest(
    val product_id: String,
    val product_name: String,
    val quantity: Double,
    val unit: String,
    val unit_price: Double,
    val total_cost: Double,
    val estimated_size: Double? = null,
    val estimated_weight: Double? = null,
    val fingerling_age: Double? = null
) {
    companion object {
        fun fromDomainModel(domainModel: FarmInputCost) = NetworkFarmInputRequest(
            product_id = domainModel.productId,
            product_name = domainModel.productId,
            quantity = domainModel.amount,
            unit = domainModel.productId,
            unit_price = domainModel.unitPrice.toDouble(),
            total_cost = domainModel.totalCost.toDouble(),
            estimated_size = domainModel.fingerlingSize?.toDouble(),
            estimated_weight = domainModel.fingerlingWeight?.toDouble(),
            fingerling_age = domainModel.fingerlingAge?.toDouble(),
        )
    }
}