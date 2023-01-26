package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductFilter
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmInputProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmInputProductsStreamUseCase @Inject
constructor(
    private val farmInputProductRepository: FarmInputProductRepository
) {
    operator fun invoke(request: GetFarmInputProductsRequest): Flow<List<FarmInputProduct>> {
        return farmInputProductRepository.getFarmInputProductsStream(
            query = request.filter?.query.orEmpty(),
            categoryId = request.filter?.categoryId.orEmpty()
        )
    }

    data class GetFarmInputProductsRequest(
        val filter: FarmInputProductFilter?
    )
}