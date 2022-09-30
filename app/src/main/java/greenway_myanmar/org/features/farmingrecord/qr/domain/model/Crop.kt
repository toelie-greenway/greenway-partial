package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class Crop(
    val id: String,
    val title: String,
) {
    companion object {
        val Empty = Crop(
            id = "",
            title = ""
        )
    }
}