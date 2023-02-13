package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput

import com.greenwaymyanmar.core.presentation.model.UiUnitOfMeasurement
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import java.math.BigDecimal

data class FarmInputInputUiState(
    val product: UiFarmInputProduct? = null,
    val productError: Text? = null,

    val usedAmount: String? = null,
    val usedAmountError: Text? = null,
    val usedUnit: UiUnitOfMeasurement? = null,
    val usedUnitError: Text? = null,
    val usedUnitPrice: String? = null,
    val usedUnitPriceError: Text? = null,

    val fingerlingWeight: String? = null,
    val fingerlingSize: String? = null,
    val fingerlingAge: String? = null,
    val fingerlingWeightError: Text? = null,
    val fingerlingSizeError: Text? = null,
    val fingerlingAgeError: Text? = null,

    val amountPerPackage: String? = null,
    val amountPerPackageError: Text? = null,
    val packageUnit: String? = null,
    val packageUnitError: Text? = null,
    val packageUnitPrice: String? = null,
    val packageUnitPriceError: String? = null,

    val totalCost: BigDecimal? = null,
    val totalCostError: Text? = null,

    val inputResult: UiFarmInputCost? = null
) {
    val isFingerling: Boolean = product?.isFingerling ?: false
}

data class FarmInputInputResult(
    val product: UiFarmInputProduct,
    val usedAmount: Double = 0.0,
    val usedUnit: UiUnitOfMeasurement,
    val usedUnitPrice: BigDecimal,
    val fingerlingWeight: BigDecimal? = null,
    val fingerlingSize: BigDecimal? = null,
    val fingerlingAge: BigDecimal? = null,
    val totalCost: BigDecimal
)