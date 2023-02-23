package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Expense
import greenway_myanmar.org.util.DateUtils
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.math.BigDecimal

data class UiExpense(
    val id: String,
    val date: Instant,
    val labourQuantity: Int? = null,
    val labourCost: BigDecimal? = null,
    val familyQuantity: Int? = null,
    val familyCost: BigDecimal? = null,
    val machineryCost: BigDecimal? = null,
    val totalCost: BigDecimal? = null,
    val photos: List<String>? = null,
    val remark: String? = null,
    val inputs: List<UiFarmInputCost>? = null
) {
    val formattedDate = DateUtils.format(date.toJavaInstant(), "dd MMMM၊ yyyy")

    fun hasRemark(): Boolean {
        return !remark.isNullOrEmpty() || !photos.isNullOrEmpty()
    }

    fun hasRemarkText(): Boolean {
        return !remark.isNullOrEmpty()
    }

    fun hasRemarkPhoto(): Boolean {
        return !photos.isNullOrEmpty()
    }

    companion object {
        fun fromDomainModel(domainModel: Expense) = UiExpense(
           id  = domainModel.id,
             date = domainModel.date,
             labourQuantity = domainModel.labourQuantity,
             labourCost = domainModel.labourCost,
             familyQuantity = domainModel.familyQuantity,
             familyCost = domainModel.familyCost,
             machineryCost = domainModel.machineryCost,
             totalCost = domainModel.totalCost,
             photos = domainModel.photos,
             remark = domainModel.remark,
             inputs = domainModel.inputs.orEmpty().map {
                UiFarmInputCost.fromDomainModel(it)
             }
        )
    }
}