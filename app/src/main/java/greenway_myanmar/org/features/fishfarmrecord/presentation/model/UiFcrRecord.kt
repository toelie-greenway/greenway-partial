package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import kotlinx.datetime.Instant

data class UiFcrRecord(
    val date: Instant,
    val ratios: List<UiFcr>
)