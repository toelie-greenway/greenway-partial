package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiExpenseCategory(
    val id: String,
    val name: String
) : Parcelable {
    companion object {
        fun fromDomain(domainModel: ExpenseCategory) = UiExpenseCategory(
            id = domainModel.id,
            name = domainModel.name
        )
    }
}