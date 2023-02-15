package greenway_myanmar.org.features.fishfarmrecord.domain.model

import kotlinx.datetime.Instant
import java.math.BigDecimal

data class CropIncome(
    val id: String,
    val date: Instant,
    val income: BigDecimal,
    val crop: Crop
)