package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrAddEditExpenseFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.labourcost.LabourCostInputBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.machinerycost.MachineryCostInputBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views.ExpenseCategoryInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views.FarmInputListInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views.LabourCostInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views.MachineryCostInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker.ExpenseCategoryPickerBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import greenway_myanmar.org.ui.widget.GreenWayDateInputView.OnDateChangeListener
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class AddEditExpenseFragment : Fragment(R.layout.ffr_add_edit_expense_fragment) {

    private val viewModel: AddEditExpenseViewModel by viewModels()
    private var binding: FfrAddEditExpenseFragmentBinding by autoCleared()

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
        setFragmentResultListener(
            LabourCostInputBottomSheetFragment.REQUEST_KEY_LABOUR_COST
        ) { _, bundle ->
            val cost = bundle.getParcelableExtraCompat<UiLabourCost>(
                LabourCostInputBottomSheetFragment.KEY_LABOUR_COST
            )
            if (cost != null) {
                viewModel.handleEvent(
                    AddEditExpenseEvent.OnLabourCostChanged(cost)
                )
            }
        }
        setFragmentResultListener(
            MachineryCostInputBottomSheetFragment.REQUEST_KEY_MACHINERY_COST
        ) { _, bundle ->
            val cost = bundle.getParcelableExtraCompat<UiMachineryCost>(
                MachineryCostInputBottomSheetFragment.KEY_MACHINERY_COST
            )
            if (cost != null) {
                viewModel.handleEvent(
                    AddEditExpenseEvent.OnMachineryCostChanged(cost)
                )
            }
        }
    }

    private fun setupUi() {
        setupDateInputUi()
        setupCategoryInputUi()
        setupLabourCostInputUi()
        setupMachineryCostInputUi()
        setupFarmInputListInputUi()
        setupNoteInputUi()
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

    private fun setupLabourCostInputUi() {
        binding.labourCostInputView.setClickCall(object : LabourCostInputView.ClickCallback {
            override fun onClick() {
                openLabourCostInputDialog()
            }
        })
    }

    private fun setupMachineryCostInputUi() {
        binding.machineryCostInputView.setClickCall(object : MachineryCostInputView.ClickCallback {
            override fun onClick() {
                openMachineryCostInputDialog()
            }
        })
    }

    private fun setupFarmInputListInputUi() {
        binding.farmInputListInputView.setOnItemClickListener(object :
            FarmInputListInputView.OnItemClickListener {
            override fun onAddNewFarmInput() {
                openFarmInputScreen()
            }

            override fun onFarmInputItemClick() {

            }
        })
    }

    private fun setupNoteInputUi() {
        binding.noteTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                AddEditExpenseEvent.OnNoteChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeDate()
            observeCategory()
            observeLabourCost()
            observeMachineryCost()
            observeFarmInputs()
            observeNote()
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

    private fun CoroutineScope.observeLabourCost() = launch {
        viewModel.uiState.map { it.labourCost }
            .distinctUntilChanged()
            .collect {
                binding.labourCostInputView.bind(it)
            }
    }

    private fun CoroutineScope.observeMachineryCost() = launch {
        viewModel.uiState.map { it.machineryCost }
            .distinctUntilChanged()
            .collect {
                binding.machineryCostInputView.bind(it)
            }
    }

    private fun CoroutineScope.observeFarmInputs() = launch {
        viewModel.uiState.map { it.farmInputs }
            .distinctUntilChanged()
            .collect {
                binding.farmInputListInputView.setItems(it)
            }
    }

    private fun CoroutineScope.observeNote() = launch {
        viewModel.uiState.map { it.note }
            .distinctUntilChanged()
            .collect {
                binding.noteTextInputEditText.bindText(it)
            }
    }

    private fun openCategoryPickerDialog() {
        findNavController().navigate(
            AddEditExpenseFragmentDirections
                .actionAddEditExpenseFragmentToExpenseCategoryPickerBottomSheetFragment()
        )
    }

    private fun openLabourCostInputDialog() {
        findNavController().navigate(
            AddEditExpenseFragmentDirections
                .actionAddEditExpenseFragmentToLabourCostInputBottomSheetFragment()
        )
    }

    private fun openMachineryCostInputDialog() {
        findNavController().navigate(
            AddEditExpenseFragmentDirections
                .actionAddEditExpenseFragmentToMachineryCostInputBottomSheetFragment()
        )
    }

    private fun openFarmInputScreen() {
        findNavController().navigate(
            AddEditExpenseFragmentDirections
                .actionAddEditExpenseFragmentToFarmInputInputFragment()
        )
    }
}
