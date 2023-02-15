package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFish
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ProductionRecordRepository
import kotlinx.datetime.Instant
import java.math.BigDecimal
import javax.inject.Inject

class SaveProductionRecordUseCase @Inject constructor(
    private val repository: ProductionRecordRepository
) {
    suspend operator fun invoke(request: SaveProductionRecordRequest) : SaveProductionRecordResult {
        return repository.saveProductionRecord(request)
    }

    data class SaveProductionRecordRequest(
        val id: String?,
        val seasonId: String,
        val date: Instant,
        val productionsPerFish: List<ProductionPerFish>
    ) {
        val total: BigDecimal = productionsPerFish.sumOf {  productionPerFish ->
            productionPerFish.productionsPerFishSize.sumOf { it.price * it.weight }
        }
    }

    data class SaveProductionRecordResult(
        val id: String
    )
}