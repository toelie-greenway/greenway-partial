package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import android.content.Context
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.MyanmarZarConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.math.BigDecimal
import java.text.NumberFormat

private val numberFormat: NumberFormat = NumberFormat.getInstance(MyanmarZarConverter.getLocale())

data class FcrRecordListItemUiState(
    val id: String,
    val date: Instant,
    val calculatedRatio: BigDecimal,
    val totalFeedWeight: BigDecimal,
    val totalGainWeight: BigDecimal
) {
    fun formattedDate(): String = DateUtils.format(date.toJavaInstant(), "MMM d ၊ yyyy · E")

    fun formattedCalculatedRatio(context: Context) = context.resources.getString(
        R.string.ffr_fcr_label_formatted_ratio, numberFormat.format(calculatedRatio)
    )

    fun formattedTotalFeedWeight(context: Context) = context.resources.getString(
        R.string.ffr_fcr_label_formatted_weight_with_symbol, numberFormat.format(totalFeedWeight)
    )

    fun formattedTotalGainWeight(context: Context) = context.resources.getString(
        R.string.ffr_fcr_label_formatted_weight_with_symbol, numberFormat.format(totalGainWeight)
    )

    companion object {
        fun fromDomainModel(domainModel: FcrRecord) = FcrRecordListItemUiState(
            id = domainModel.id,
            date = domainModel.date,
            calculatedRatio = domainModel.ratios.sumOf { it.ratio },
            totalFeedWeight = domainModel.ratios.sumOf { it.feedWeight },
            totalGainWeight = domainModel.ratios.sumOf { it.gainWeight },
        )
    }
}