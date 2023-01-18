package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrAddEditExpenseFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views.ExpenseCategoryInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.AddEditSeasonUiEvent
import greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker.ExpenseCategoryPickerBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput.FishInputFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.ui.widget.GreenWayDateInputView.OnDateChangeListener
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class AddEditExpenseFragment : Fragment(R.layout.ffr_add_edit_expense_fragment) {

    private val viewModel: AddEditExpenseViewModel by viewModels()
    private var binding: FfrAddEditExpenseFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrAddEditExpenseFragmentBinding.bind(view)
        setupFragmentResultListener()
        setupUi()
        observeViewModel()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(
            ExpenseCategoryPickerBottomSheetFragment.REQUEST_KEY_EXPENSE_CATEGORY
        ) { _, bundle ->
            val category = bundle.getParcelableExtraCompat<UiExpenseCategory>(
                ExpenseCategoryPickerBottomSheetFragment.KEY_EXPENSE_CATEGORY
            )
            if (category != null) {
                viewModel.handleEvent(
                    AddEditExpenseEvent.OnCategoryChanged(category)
                )
            }
        }
    }

    private fun setupUi() {
        setupDateInputUi()
        setupCategoryInputUi()
    }

    private fun setupDateInputUi() {
        binding.dateInputView.onDateChangeListener = object : OnDateChangeListener {
            override fun onDateChanged(date: LocalDate) {
                viewModel.handleEvent(AddEditExpenseEvent.OnDateChanged(date))
            }
        }
    }

    private fun setupCategoryInputUi() {
        binding.categoryInputView.setClickCall(object : ExpenseCategoryInputView.ClickCallback {
            override fun onClick() {
                openCategoryPickerDialog()
            }
        })
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeDate()
            observeCategory()
        }
    }

    private fun CoroutineScope.observeDate() = launch {
        viewModel.uiState.map { it.date }
            .distinctUntilChanged()
            .collect {
                binding.dateInputView.bindDate(it)
            }
    }

    private fun CoroutineScope.observeCategory() = launch {
        viewModel.uiState.map { it.category }
            .distinctUntilChanged()
            .collect {
                binding.categoryInputView.category = it
            }
    }

    private fun openCategoryPickerDialog() {
        findNavController().navigate(
            AddEditExpenseFragmentDirections
                .actionAddEditExpenseFragmentToExpenseCategoryPickerBottomSheetFragment()
        )
    }

}
