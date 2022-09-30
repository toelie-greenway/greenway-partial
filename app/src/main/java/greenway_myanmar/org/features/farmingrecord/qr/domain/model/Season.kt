package greenway_myanmar.org.features.farmingrecord.qr.domain.model

import java.time.Instant

data class Season(
    val id: String,
    val seasonName: String,
    val crop: Crop,
    val latestHarvestedDate: Instant? = null
) {
    companion object {
        val Empty = Season(
            id = "",
            seasonName = "",
            crop = Crop.Empty
        )
    }
}