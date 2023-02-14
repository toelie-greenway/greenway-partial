package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFish
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fcr
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordRequest
import greenway_myanmar.org.util.toServerDateString
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFcrRecordRequest(
    val season_id: String,
    val date: String,
    val record: List<NetFcrRequest>
) {
    companion object {
        fun fromDomainRequest(request: SaveFcrRecordRequest) =
            NetworkFcrRecordRequest(
                season_id = request.seasonId,
                date = request.date.toServerDateString(),
                record = request.ratios.map {
                    NetFcrRequest.fromDomainRequest(it)
                }
            )
    }
}

@Serializable
data class NetFcrRequest(
    val fish: NetworkFish,
    val total_feed: Double,
    val total_weight_gain: Double
) {
    companion object {
        fun fromDomainRequest(request: Fcr) = NetFcrRequest(
            fish = NetworkFish.fromDomainModel(request.fish),
            total_feed = request.feedWeight.toDouble(),
            total_weight_gain = request.gainWeight.toDouble()
        )
    }
}