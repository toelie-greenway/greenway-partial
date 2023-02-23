package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.content.Context
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncome
import greenway_myanmar.org.util.DateUtils
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.math.BigDecimal

data class UiCropIncome(
    val id: String,
    val date: Instant,
    val income: BigDecimal,
    val crop: UiCrop
) {

    val formattedDate = DateUtils.format(date.toJavaInstant(), "dd MMMM၊ yyyy")
    fun formattedIncome(context: Context) =
        context.getString(R.string.formatted_kyat, numberFormat.format(income))

    companion object {
        fun fromDomainModel(domainModel: CropIncome) = UiCropIncome(
            id = domainModel.id,
            date = domainModel.date,
            income = domainModel.income,
            crop =  UiCrop.fromDomainModel(domainModel.crop)
        )
    }
}