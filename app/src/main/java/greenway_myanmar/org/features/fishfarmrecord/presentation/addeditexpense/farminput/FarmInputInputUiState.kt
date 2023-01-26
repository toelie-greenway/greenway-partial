package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.farminput

import com.greenwaymyanmar.core.presentation.model.UiUnitOfMeasurement
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import java.math.BigDecimal

//var amount: Double = 0.0,
//var unit: String = "",
//var unitPrice: Int,
//var amountPerPackage: Double? = null,
//var packageUnit: String? = null,
//var totalCost: BigDecimal

data class FarmInputInputUiState(
    val product: UiFarmInputProduct? = null,
    val productError: Text? = null,

    val isAdvanced: Boolean = false,

    val usedAmount: String? = null,
    val usedAmountError: Text? = null,
    val usedUnit: UiUnitOfMeasurement? = null,
    val usedUnitError: Text? = null,
    val usedUnitPrice: String? = null,
    val usedUnitPriceError: Text? = null,

    val amountPerPackage: String? = null,
    val amountPerPackageError: Text? = null,
    val packageUnit: String? = null,
    val packageUnitError: Text? = null,
    val packageUnitPrice: String? = null,
    val packageUnitPriceError: String? = null,

    val totalCost: BigDecimal? = null,
    val totalCostError: Text? = null,

    val inputResult: FarmInputInputResult? = null
)

data class FarmInputInputResult(
    val product: UiFarmInputProduct,
    val usedAmount: Double = 0.0,
    val usedUnit: UiUnitOfMeasurement,
    val usedUnitPrice: BigDecimal,
    val amountPerPackage: Double = 0.0,
    val packageUnit: String = "",
    val packageUnitPrice: BigDecimal = BigDecimal.ZERO,
    val totalCost: BigDecimal
)