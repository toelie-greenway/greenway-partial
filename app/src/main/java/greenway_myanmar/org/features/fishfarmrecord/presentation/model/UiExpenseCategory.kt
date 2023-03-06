package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiExpenseCategory(
    val id: String,
    val name: String,
    val isHarvesting: Boolean,
    val isGeneralExpenseCategory: Boolean
) : Parcelable {
    companion object {
        fun fromDomain(domainModel: ExpenseCategory) = UiExpenseCategory(
            id = domainModel.id,
            name = domainModel.name,
            isHarvesting = domainModel.isHarvesting,
            isGeneralExpenseCategory = domainModel.isGeneralExpenseCategory
        )
    }
}

fun UiExpenseCategory.asDomainModel() = ExpenseCategory(
    id = id,
    name = name,
    isHarvesting = isHarvesting,
    isGeneralExpenseCategory = isGeneralExpenseCategory
)