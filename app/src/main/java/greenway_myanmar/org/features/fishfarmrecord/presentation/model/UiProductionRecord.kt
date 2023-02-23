package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.content.Context
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecord
import greenway_myanmar.org.util.DateUtils
import kotlinx.datetime.toJavaInstant
import java.math.BigDecimal
import java.time.Instant

data class UiProductionRecord(
    val id: String,
    val date: Instant,
    val totalPrice: BigDecimal,
    val totalWeight: BigDecimal,
    val productions: List<UiProductionPerFish>,
    val note: String
) {

    val formattedDate = DateUtils.format(date, "dd MMMM၊ yyyy")

    fun formattedTotalPrice(context: Context) =
        context.getString(R.string.formatted_kyat, numberFormat.format(totalPrice))

    fun formattedTotalWeight(context: Context) =
        context.getString(R.string.formatted_viss, numberFormat.format(totalWeight))

    companion object {
        fun fromDomainModel(domainModel: ProductionRecord) = UiProductionRecord(
            id = domainModel.id,
            date = domainModel.date.toJavaInstant(),
            totalPrice = domainModel.totalPrice,
            totalWeight = domainModel.totalWeight,
            productions = domainModel.productions.map(UiProductionPerFish.Companion::fromDomainModel),
            note = domainModel.note
        )
    }
}