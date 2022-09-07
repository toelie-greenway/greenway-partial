package greenway_myanmar.org.features.farmingrecord.qr.domain.model

enum class FarmLocationType(val value: String) {
    Map("map"),
    Village("village"),
    Township("township");

    companion object {
        fun fromString(value: String): FarmLocationType {
            return when (value) {
                "map" -> Map
                "village" -> Village
                "township" -> Township
                else -> Map
            }
        }
    }
}

