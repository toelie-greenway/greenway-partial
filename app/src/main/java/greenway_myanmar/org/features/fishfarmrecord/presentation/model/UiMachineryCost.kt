package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class UiMachineryCost(
    val totalCost: BigDecimal = BigDecimal.ZERO
) : Parcelable
