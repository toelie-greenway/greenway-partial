package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.labourcost

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import java.math.BigDecimal

data class LabourCostInputUiState(
    val labourQuantity: String? = null,
    val labourCost: String? = null,
    val familyMemberQuantity: String? = null,
    val familyMemberCost: String? = null,
    val totalCost: BigDecimal = BigDecimal.ZERO,

    val labourQuantityError: Text? = null,
    val labourCostError: Text? = null,
    val animalResourceQuantityError: Text? = null,
    val animalResourceCostError: Text? = null,

    val inputResult: UiLabourCost? = null
)