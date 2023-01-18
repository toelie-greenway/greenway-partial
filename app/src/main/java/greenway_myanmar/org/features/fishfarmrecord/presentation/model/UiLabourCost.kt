package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiLabourCost(
    val labourQuantity: Int = 0,
    val labourCost: Int = 0,
    val animalResourceQuantity: Int = 0,
    val animalResourceCost: Int = 0,
    val familyMemberQuantity: Int = 0,
    val familyMemberCost: Int = 0
) : Parcelable {

    val total: Int
        get() = labourCost + animalResourceCost

    companion object {
        const val KEY_LABOUR_QTY = "labour_qty"
        const val KEY_LABOUR_COST = "labour_cost"
        const val KEY_FAMILY_QTY = "family_qty"
        const val KEY_FAMILY_COST = "family_cost"
        const val KEY_ANIMAL_QTY = "animal_qty"
        const val KEY_ANIMAL_COST = "animal_cost"
    }
}
