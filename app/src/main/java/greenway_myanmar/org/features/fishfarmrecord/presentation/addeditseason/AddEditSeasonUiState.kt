package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLoanDuration
import java.time.LocalDate

data class AddEditSeasonUiState(
    val editFarmMeasurement: Boolean = false,
    val farmArea: String? = null,
    val farmAreaError: Text? = null,
    val farmMeasurement: AreaMeasurement? = null,

    val seasonName: String? = null,
    val seasonStartDate: LocalDate = LocalDate.now(),
    val fishes: List<UiFish> = emptyList(),

    val seasonNameError: Text? = null,
    val seasonStartDateError: Text? = null,
    val fishesError: Text? = null,

    val companyCode: String? = null,
    val companyCodeError: Text? = null,
    val company: UiContractFarmingCompany? = null,

    val inputLoanInformation: Boolean = false,
    val loanAmount: String? = null,
    val loanDuration: UiLoanDuration? = null,
    val loanOrganization: String? = null,
    val loanRemark: String? = null,

    val loanAmountError: Text? = null,
    val loanDurationError: Text? = null,
    val loanOrganizationError: Text? = null,

    val addEditSeasonResult: AddEditSeasonResult? = null
) {
    data class AddEditSeasonResult(
        val seasonId: String
    )
}

typealias CompanyUiState = LoadingState<UiContractFarmingCompany>