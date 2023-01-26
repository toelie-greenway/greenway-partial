package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import java.math.BigDecimal
import javax.inject.Inject

class CalculateSimpleFarmInputCostUseCase @Inject constructor() {

    operator fun invoke(request: CalculateSimpleFarmInputCostRequest): CalculateSimpleFarmInputCostResult {
        val unitPrice = request.unitPrice
        val amount = BigDecimal.valueOf(request.amount)
        return CalculateSimpleFarmInputCostResult(
            total = unitPrice * amount
        )
    }

    data class CalculateSimpleFarmInputCostRequest(
        val unitPrice: BigDecimal,
        val amount: Double
    )

    data class CalculateSimpleFarmInputCostResult(val total: BigDecimal)

}