package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import java.math.BigDecimal
import javax.inject.Inject

class CalculateAdvancedFarmInputCostUseCase @Inject constructor() {

    operator fun invoke(request: CalculateAdvancedFarmInputCostRequest): CalculateAdvancedFarmInputCostResult {
        val usedUnitPrice = request.pricePerPackage / BigDecimal.valueOf(request.amountPerPackage)
        val usedAmount = BigDecimal.valueOf(request.usedAmount)
        return CalculateAdvancedFarmInputCostResult(
            total = usedUnitPrice * usedAmount
        )
    }

    data class CalculateAdvancedFarmInputCostRequest(
        val pricePerPackage: BigDecimal,
        val amountPerPackage: Double,
        val usedAmount: Double
    )

    data class CalculateAdvancedFarmInputCostResult(val total: BigDecimal)

}