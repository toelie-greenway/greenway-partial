package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.presentation.extensions.hideSoftInput
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.FfrAddEditExpenseFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.FarmInputInputFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.labourcost.LabourCostInputBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.machinerycost.MachineryCostInputBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.views.ExpenseCategoryInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.views.FarmInputListInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.views.LabourCostInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.views.MachineryCostInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker.ExpenseCategoryPickerBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import greenway_myanmar.org.ui.widget.GreenWayDateInputView.OnDateChangeListener
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.extensions.requireNetworkConnection
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class AddEditExpenseFragment : Fragment(R.layout.ffr_add_edit_expense_fragment) {

    private val viewModel: AddEditExpenseViewModel by viewModels()
    private val binding by viewBinding(FfrAddEditExpenseFragmentBinding::bind)
    private val args by navArgs<AddEditExpenseFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenTransactionName(view)
        setupFragmentResultListener()
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupToolbar()
        setupDateInputUi()
        setupCategoryInputUi()
        setupLabourCostInputUi()
        setupMachineryCostInputUi()
        setupFarmInputListInputUi()
        setupGeneralExpenseCategoryInputUi()
        setupGeneralExpenseInputUi()
        setupNoteInputUi()
        setupSubmitButton()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeIsGeneralExpenseCategory()
            observeGeneralExpenseCategories()
            observeGeneralExpenseCategory()
            observeGeneralExpenseCategoryError()
            observeDate()
            observeCategory()
            observeCategoryError()
            observeLabourCost()
            observeMachineryCost()
            observeFarmInputs()
            observeGeneralExpenses()
            observeGeneralExpensesError()
            observeNote()
            observeCostError()
            observeSeasonUploadingState()
            observeAddEditExpenseResult()
        }
    }

    private fun setupTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }

    private fun setScreenTransactionName(view: View) {
        ViewCompat.setTransitionName(
            view,
            getString(R.string.ffr_transition_name_screen_add_edit_expense)
        )
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
                    AddEditExpenseEvent.OnCategoryChanged(
                        category
                    )
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
                    AddEditExpenseEvent.OnLabourCostChanged(
                        cost
                    )
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
                    AddEditExpenseEvent.OnMachineryCostChanged(
                        cost
                    )
                )
            }
        }
        setFragmentResultListener(
            FarmInputInputFragment.REQUEST_KEY_FARM_INPUT_COST
        ) { _, bundle ->
            val cost = bundle.getParcelableExtraCompat<UiFarmInputCost>(
                FarmInputInputFragment.KEY_FARM_INPUT_COST
            )
            if (cost != null) {
                viewModel.handleEvent(
                    AddEditExpenseEvent.OnFarmInputAdded(
                        cost
                    )
                )
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupDateInputUi() {
        binding.dateInputView.onDateChangeListener = object : OnDateChangeListener {
            override fun onDateChanged(date: LocalDate) {
                viewModel.handleEvent(
                    greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent.OnDateChanged(
                        date
                    )
                )
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

    private fun setupGeneralExpenseCategoryInputUi() {
        binding.generalExpenseCategoryAutoCompleteTextView.apply {
            setAdapter(
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.greenway_dropdown_menu_popup_item,
                    mutableListOf()
                )
            )
            onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    viewModel.handleEvent(
                        AddEditExpenseEvent.OnGeneralExpenseCategorySelectionChanged(
                            position
                        )
                    )
                }
        }
    }

    private fun setupLabourCostInputUi() {
        binding.labourCostInputView.setClickCall(object : LabourCostInputView.ClickCallback {
            override fun onClick() {
                openLabourCostInputDialog()
            }
        })
    }

    private fun setupMachineryCostInputUi() {
        binding.machineryCostInputView.setClickCall(object :
            MachineryCostInputView.ClickCallback {
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

            override fun onRemoveFarmInput(item: UiFarmInputCost) {
                viewModel.handleEvent(AddEditExpenseEvent.OnFarmInputRemoved(item))
            }

            override fun onFarmInputItemClick() {

            }
        })
    }

    private fun setupGeneralExpenseInputUi() {
        binding.generalExpensesTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                AddEditExpenseEvent.OnGeneralExpenseChanged(
                    it?.toString().orEmpty()
                )
            )
        }
    }

    private fun setupNoteInputUi() {
        binding.noteTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                AddEditExpenseEvent.OnNoteChanged(
                    it?.toString().orEmpty()
                )
            )
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            hideSoftInput()
            requireNetworkConnection {
                viewModel.handleEvent(AddEditExpenseEvent.OnSubmit)
            }
        }
    }

    private fun CoroutineScope.observeIsGeneralExpenseCategory() = launch {
        viewModel.uiState.map { it.isGeneralExpenseCategory }
            .distinctUntilChanged()
            .collect { isGeneralExpenseCategory ->
                showHideInputs(isGeneralExpenseCategory)
            }
    }

    private fun CoroutineScope.observeGeneralExpenseCategories() = launch {
        viewModel.generalExpenseCategoriesUiState
            .collect { uiState ->
                when (uiState) {
                    is LoadingState.Success -> {
                        binding.generalExpenseCategoryAutoCompleteTextView.setAdapter(
                            ArrayAdapter(
                                requireContext(),
                                R.layout.greenway_dropdown_menu_popup_item,
                                uiState.data.map { it.name }
                            )
                        )
                    }
                    else -> {
                        // no-op
                    }
                }
            }
    }

    private fun CoroutineScope.observeGeneralExpenseCategory() = launch {
        viewModel.uiState.map { it.generalExpenseCategory }
            .distinctUntilChanged()
            .collect { category ->
                binding.generalExpenseCategoryAutoCompleteTextView.setText(
                    category?.name.orEmpty(),
                    false
                )
            }
    }

    private fun CoroutineScope.observeGeneralExpenseCategoryError() = launch {
        viewModel.uiState.map { it.generalExpenseCategoryError }
            .distinctUntilChanged()
            .collect {
                binding.generalExpenseCategoryDropdownMenu.setError(it)
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

    private fun CoroutineScope.observeCategoryError() = launch {
        viewModel.uiState.map { it.categoryError }
            .distinctUntilChanged()
            .collect {
                binding.categoryInputView.setError(it)
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
        viewModel.uiState.map { it.farmInputCosts }
            .distinctUntilChanged()
            .collect {
                binding.farmInputListInputView.setItems(it)
            }
    }

    private fun CoroutineScope.observeGeneralExpenses() = launch {
        viewModel.uiState.map { it.generalExpense }
            .distinctUntilChanged()
            .collect {
                binding.generalExpensesTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeGeneralExpensesError() = launch {
        viewModel.uiState.map { it.generalExpenseError }
            .distinctUntilChanged()
            .collect {
                binding.generalExpensesTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeNote() = launch {
        viewModel.uiState.map { it.note }
            .distinctUntilChanged()
            .collect {
                binding.noteTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeCostError() = launch {
        viewModel.uiState.map { it.costError }
            .distinctUntilChanged()
            .collect { error ->
                if (error != null) {
                    showCostRequiredError(error)
                }
            }
    }

    private fun CoroutineScope.observeSeasonUploadingState() = launch {
        launch {
            viewModel.expenseUploadingUiState
                .collect { uiState ->
                    binding.submitButton.isVisible = uiState !is LoadingState.Loading
                    binding.expenseUploadingContainer.isVisible = uiState is LoadingState.Loading
                    if (uiState is LoadingState.Error && uiState.message != null) {
                        showUploadingSeasonError(uiState.message)
                    }
                }
        }
    }

    private fun CoroutineScope.observeAddEditExpenseResult() = launch {
        launch {
            viewModel.uiState.map { it.addEditExpenseResult }
                .distinctUntilChanged()
                .collect { result ->
                    if (result != null) {
                        findNavController().popBackStack()
                    }
                }
        }
    }

    private fun showHideInputs(generalExpenseCategory: Boolean) {
        binding.generalExpensesTextInputLayout.isVisible = generalExpenseCategory
        binding.generalExpenseCategoryDropdownMenu.isVisible = generalExpenseCategory
        binding.labourCostInputView.isVisible = !generalExpenseCategory
        binding.machineryCostInputView.isVisible = !generalExpenseCategory
        binding.farmInputListInputView.isVisible = !generalExpenseCategory
    }

    private fun showCostRequiredError(error: Text) {
        showSnackbar(error)
        viewModel.handleEvent(AddEditExpenseEvent.CostErrorShown)
    }

    private fun openCategoryPickerDialog() {
        findNavController().navigate(
            AddEditExpenseFragmentDirections
                .actionAddEditExpenseFragmentToExpenseCategoryPickerBottomSheetFragment(args.seasonId)
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

    private fun showUploadingSeasonError(message: Text) {
        showSnackbar(message)
        viewModel.handleEvent(AddEditExpenseEvent.OnExpenseUploadingErrorShown)
    }
}
