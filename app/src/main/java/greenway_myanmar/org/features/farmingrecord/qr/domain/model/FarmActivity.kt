package greenway_myanmar.org.features.farmingrecord.qr.domain.model

import java.time.Instant

data class FarmActivity(
    val categoryId: String,
    val categoryTitle: String,
    val date: Instant,
    val farmInputs: List<FarmInput>
)