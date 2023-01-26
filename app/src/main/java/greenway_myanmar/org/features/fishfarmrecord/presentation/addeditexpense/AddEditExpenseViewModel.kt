package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditExpenseUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: AddEditExpenseUiState
        get() = uiState.value

    fun handleEvent(event: AddEditExpenseEvent) {
        when (event) {
            is AddEditExpenseEvent.OnDateChanged -> {
                updateDate(event.date)
            }
            is AddEditExpenseEvent.OnCategoryChanged -> {
                updateCategory(event.category)
            }
            is AddEditExpenseEvent.OnLabourCostChanged -> {
                updateLabourCost(event.labourCost)
            }
            is AddEditExpenseEvent.OnMachineryCostChanged -> {
                updateMachineryCost(event.machineryCost)
            }
            is AddEditExpenseEvent.OnFarmInputAdded -> {
                updateFarmInput(event.farmInput)
            }
            is AddEditExpenseEvent.OnNoteChanged -> {
                updateNote(event.note)
            }
        }
    }

    private fun updateDate(date: LocalDate) {
        _uiState.value = currentUiState.copy(date = date)
    }

    private fun updateCategory(category: UiExpenseCategory) {
        _uiState.value = currentUiState.copy(category = category)
    }

    private fun updateLabourCost(labourCost: UiLabourCost) {
        _uiState.value = currentUiState.copy(labourCost = labourCost)
    }

    private fun updateMachineryCost(machineryCost: UiMachineryCost) {
        _uiState.value = currentUiState.copy(machineryCost = machineryCost)
    }

    private fun updateFarmInput(farmInput: UiFarmInputCost) {
        _uiState.value = currentUiState.copy(
            farmInputs = currentUiState.farmInputs
                .removeIfExists(farmInput)
                .add(farmInput)
        )
    }

    private fun List<UiFarmInputCost>.removeIfExists(
        item: UiFarmInputCost,
    ): List<UiFarmInputCost> {
        val resultList = this.toMutableList()
        val found = currentUiState.farmInputs.find { it.productId == item.productId }
        if (found != null) {
            resultList.remove(found)
        }
        return resultList
    }

    private fun List<UiFarmInputCost>.add(item: UiFarmInputCost): List<UiFarmInputCost> {
        val resultList = this.toMutableList()
        resultList.add(item)
        return resultList
    }

    private fun updateNote(note: String) {
        _uiState.value = currentUiState.copy(note = note)
    }
}