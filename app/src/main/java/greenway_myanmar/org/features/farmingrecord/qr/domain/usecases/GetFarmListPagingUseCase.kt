package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.FarmRepository
import greenway_myanmar.org.vo.Listing
import javax.inject.Inject

class GetFarmListPagingUseCase @Inject constructor(
    private val farmRepository: FarmRepository
) {

    operator fun invoke(): Listing<Farm> {
        return farmRepository.getFarmListing()
    }

}