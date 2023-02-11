package greenway_myanmar.org.features.fishfarmrecord.presentation.season.addeditseason.loanduration

import greenway_myanmar.org.common.domain.entities.Text

data class CustomLoanDurationUiState(
    val month: String? = null,
    val monthError: Text? = null,
    val inputResult: CustomLoanDurationInputResult? = null
) {
    data class CustomLoanDurationInputResult(
        val month: Int
    )
}