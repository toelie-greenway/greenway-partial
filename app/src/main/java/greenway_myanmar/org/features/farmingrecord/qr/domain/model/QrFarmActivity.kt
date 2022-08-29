package greenway_myanmar.org.features.farmingrecord.qr.domain.model

import java.time.Instant

data class QrFarmActivity(
    val activityName: String,
    val date: Instant,
    val farmInputs: String? = null
)