package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFishUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: AddEditFishUiState
        get() = uiState.value

    fun handleEvent(event: AddEditExpenseEvent) {
        when (event) {
            is AddEditExpenseEvent.OnDateChanged -> {
                updateDate(event.date)
            }
            is AddEditExpenseEvent.OnCategoryChanged -> {
                updateCategory(event.category)
            }
        }
    }

    private fun updateDate(date: LocalDate) {
        _uiState.value = currentUiState.copy(date = date)
    }

    private fun updateCategory(category: UiExpenseCategory) {
        _uiState.value = currentUiState.copy(category = category)
    }
}