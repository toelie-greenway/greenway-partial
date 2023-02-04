package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class UiLabourCost(
    val labourQuantity: Int = 0,
    val labourCost: BigDecimal = BigDecimal.ZERO,
    val familyMemberQuantity: Int = 0,
    val familyMemberCost: BigDecimal = BigDecimal.ZERO,
    val totalCost: BigDecimal = BigDecimal.ZERO,
) : Parcelable
