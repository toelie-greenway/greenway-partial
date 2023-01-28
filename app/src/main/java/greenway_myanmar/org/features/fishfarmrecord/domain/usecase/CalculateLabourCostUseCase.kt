package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import java.math.BigDecimal
import javax.inject.Inject

class CalculateLabourCostUseCase @Inject constructor() {

    operator fun invoke(request: CalculateLabourCostRequest): CalculateLabourCostResult {
        return CalculateLabourCostResult(
            total = request.labourResourceCost + request.animalResourceCost
        )
    }

    data class CalculateLabourCostRequest(
        val animalResourceCost: BigDecimal,
        val labourResourceCost: BigDecimal
    )

    data class CalculateLabourCostResult(val total: BigDecimal)

}