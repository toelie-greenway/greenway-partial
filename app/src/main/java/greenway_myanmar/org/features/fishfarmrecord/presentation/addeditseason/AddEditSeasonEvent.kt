package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLoanDuration
import java.time.LocalDate

sealed class AddEditSeasonEvent {
    data class OnSeasonNameChanged(val name: String?): AddEditSeasonEvent()
    data class OnSeasonStartDateChanged(val date: LocalDate): AddEditSeasonEvent()
    data class OnFishListChanged(val fishes: List<UiFish>): AddEditSeasonEvent()
    data class OnFishAdded(val fish: UiFish): AddEditSeasonEvent()

    data class OnCompanyCodeChanged(val code: String?): AddEditSeasonEvent()

    object OnInputLoanInformationCheckChanged: AddEditSeasonEvent()
    data class OnLoanAmountChanged(val amount: String?): AddEditSeasonEvent()
    data class OnLoanDurationChanged(val duration: UiLoanDuration?): AddEditSeasonEvent()
    data class OnLoanOrganizationChanged(val organization: String?): AddEditSeasonEvent()
    data class OnLoanRemarkChanged(val remark: String?): AddEditSeasonEvent()

    object OnSubmit: AddEditSeasonEvent()
}