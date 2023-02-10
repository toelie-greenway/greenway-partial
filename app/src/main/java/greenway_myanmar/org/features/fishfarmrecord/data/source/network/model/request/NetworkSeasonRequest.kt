package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request


import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmAreaLatLng
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveSeasonUseCase.SaveSeasonRequest
import greenway_myanmar.org.util.DateUtils
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSeasonRequest(
    val area: NetworkFarmAreaRequest,
    val company_code: String? = null,
    val fish_types: List<NetworkSeasonFishType>,
    val loan: NetworkLoanRequest? = null,
    val season: String? = null,
    val start_date: String? = null
) {
    @Serializable
    data class NetworkSeasonFishType(
        val fish_id: String,
        val specie: String?
    )

    companion object {
        fun fromDomainRequest(request: SaveSeasonRequest) = NetworkSeasonRequest(
            area = NetworkFarmAreaRequest(
                acre = request.farmMeasurement.area.value,
                measurement_type = request.farmMeasurement.measuredType?.value,
                measured_acre = request.farmMeasurement.measuredArea?.value,
                depth = request.farmMeasurement.depth,
                measurement = request.farmMeasurement.coordinates?.map {
                    NetworkFarmAreaLatLng(
                        lat = it.latitude,
                        lng = it.longitude
                    )
                },
                lat = request.farmMeasurement.location?.latitude,
                lon = request.farmMeasurement.location?.longitude,
            ),
            company_code = request.company?.code,
            fish_types = request.fishes.map {
                NetworkSeasonFishType(
                    fish_id = it.id,
                    specie = it.species
                )
            },
            loan = if (request.loan != null) NetworkLoanRequest.fromDomainModel(request.loan) else null,
            season = request.seasonName,
            start_date = DateUtils.prepareServerDateFromInstant(request.seasonStartDate.toJavaInstant())
        )
    }
}