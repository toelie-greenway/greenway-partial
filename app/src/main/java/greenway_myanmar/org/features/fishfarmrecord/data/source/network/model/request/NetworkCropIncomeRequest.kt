package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request

import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveCropIncomeUseCase.SaveCropIncomeRequest
import greenway_myanmar.org.util.toServerDateString
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCropIncomeRequest(
    val season_id: String,
    val date: String,
    val income: Double,
    val crop_id: String
) {
    companion object {
        fun fromDomainRequest(request: SaveCropIncomeRequest) = NetworkCropIncomeRequest(
            season_id = request.seasonId,
            date = request.date.toServerDateString(),
            income = request.price.toDouble(),
            crop_id = request.crop.id
        )
    }
}