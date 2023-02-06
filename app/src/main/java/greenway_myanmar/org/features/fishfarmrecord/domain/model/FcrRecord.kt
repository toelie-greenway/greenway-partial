package greenway_myanmar.org.features.fishfarmrecord.domain.model

import kotlinx.datetime.Instant

data class FcrRecord(
    val id: String,
    val date: Instant,
    val ratios: List<Fcr>
)