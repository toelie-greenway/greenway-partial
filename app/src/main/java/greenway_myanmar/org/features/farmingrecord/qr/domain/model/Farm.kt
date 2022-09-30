package greenway_myanmar.org.features.farmingrecord.qr.domain.model

const val FARM_DEFAULT_AREA_UNIT = "acre"

data class Farm(
    val id: String,
    val name: String,
    val area: Double,
    val areaUnit: String,
    val images: List<String>,
    val location: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
) {
    companion object {
        val Empty = Farm(
            id = "",
            name = "",
            area = 0.0,
            areaUnit = "",
            images = emptyList(),
            location = ""
        )
    }
}