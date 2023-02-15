package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFish
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkProductionPerFishSize
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkProductionPerFishType
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFish
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordRequest
import greenway_myanmar.org.util.toServerDateString

data class NetworkProductionRecordRequest(
    val season_id: String,
    val date: String,
    val total: Double,
    val productions: List<NetworkProductionPerFishType>
) {
    companion object {
        fun fromDomainRequest(request: SaveProductionRecordRequest) =
            NetworkProductionRecordRequest(
                season_id = request.seasonId,
                date = request.date.toServerDateString(),
                total = request.total.toDouble(),
                productions = mapProductions(request.productionsPerFish)
            )

        private fun mapProductions(productionsPerFish: List<ProductionPerFish>) =
            productionsPerFish.map {
                NetworkProductionPerFishType(
                    fish = NetworkFish.fromDomainModel(it.fish),
                    production = it.productionsPerFishSize.map { perFishSize ->
                        NetworkProductionPerFishSize(
                            size = perFishSize.fishSize.key,
                            weight = perFishSize.weight.toDouble(),
                            price = perFishSize.price.toDouble()
                        )
                    }
                )
            }

    }
}