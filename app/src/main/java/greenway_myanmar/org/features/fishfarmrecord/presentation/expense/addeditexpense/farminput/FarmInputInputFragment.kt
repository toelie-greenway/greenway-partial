package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.presentation.extensions.hideSoftInput
import greenway_myanmar.org.databinding.FfrFarmInputInputFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products.FarmInputProductPickerFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.util.extensions.bindMoney
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FarmInputInputFragment : Fragment(R.layout.ffr_farm_input_input_fragment) {

    private val viewModel: FarmInputInputViewModel by viewModels()

    private val binding by viewBinding(FfrFarmInputInputFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observePickFarmInputProductStatus()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()
        setupUi()
        observeViewModel()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(FarmInputProductPickerFragment.REQUEST_KEY_PRODUCT) { _, bundle ->
            val product =
                bundle.getParcelableExtraCompat<UiFarmInputProduct?>(FarmInputProductPickerFragment.KEY_PRODUCT)
            if (product != null) {
                viewModel.handleEvent(FarmInputInputEvent.OnFarmInputProductChanged(product))
            }
        }
    }

    private fun observePickFarmInputProductStatus() {
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(FarmInputProductPickerFragment.KEY_PICK_PRODUCT_SUCCESS)
            .observe(currentBackStackEntry) { success ->
                if (!success && viewModel.selectedProduct == null) {
                    navController.popBackStack()
                }
            }
    }

    private fun setupUi() {
        setupProductImageUi()
        setupUsedAmountInputUi()
        setupUsedUnitDropdownUi()
        setupUsedUnitPriceInputUi()
        setupFingerlingWeightInputUi()
        setupFingerlingSizeInputUi()
        setupFingerlingAgeInputUi()
        setupCancelButton()
        setupSubmitButton()
    }

    private fun setupProductImageUi() {
        binding.productThumbnailImageView.setOnClickListener {
            openProductPicker()
        }
    }

    private fun setupUsedAmountInputUi() {
        binding.usedAmountTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                FarmInputInputEvent.OnUsedAmountChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupUsedUnitDropdownUi() {
        binding.usedUnitAutoCompleteTextView.apply {
            setAdapter(
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.greenway_dropdown_menu_popup_item,
                    mutableListOf()
                )
            )
            onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    viewModel.handleEvent(FarmInputInputEvent.OnUsedUnitSelectionChanged(position))
                }
        }
    }

    private fun setupUsedUnitPriceInputUi() {
        binding.usedUnitPriceTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                FarmInputInputEvent.OnUsedUnitPriceChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupFingerlingWeightInputUi() {
        binding.fingerlingAverageWeightTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                FarmInputInputEvent.OnFingerlingWeightChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupFingerlingSizeInputUi() {
        binding.fingerlingAverageSizeTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                FarmInputInputEvent.OnFingerlingSizeChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupFingerlingAgeInputUi() {
        binding.fingerlingAgeTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                FarmInputInputEvent.OnFingerlingAgeChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupCancelButton() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            hideSoftInput()
            viewModel.handleEvent(FarmInputInputEvent.OnSubmit)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeProduct()
            observeUnits()

            observeUsedAmount()
            observeUsedAmountError()
            observeUsedUnit()
            observeUsedUnitError()
            observeUsedUnitPrice()
            observeUsedUnitPriceError()
            observeTotalCost()

            observeIsFingerling()
            observeFingerlingWeight()
            observeFingerlingSize()
            observeFingerlingAge()
            observeFingerlingWeightError()
            observeFingerlingSizeError()
            observeFingerlingAgeError()

            observeInputResult()
        }
    }

    private fun CoroutineScope.observeProduct() = launch {
        viewModel.uiState.map { it.product }
            .distinctUntilChanged()
            .collect { product ->
                if (product != null) {
                    bindProduct(product)
                } else {
                    openProductPicker()
                }
            }
    }

    private fun CoroutineScope.observeUnits() = launch {
        viewModel.unitsUiState
            .collect { uiState ->
                when (uiState) {
                    is LoadingState.Success -> {
                        binding.usedUnitAutoCompleteTextView.setAdapter(
                            ArrayAdapter(
                                requireContext(),
                                R.layout.greenway_dropdown_menu_popup_item,
                                uiState.data.map { it.unit }
                            )
                        )
                    }
                    else -> {
                        // no-op
                    }
                }
            }
    }

    private fun CoroutineScope.observeUsedAmount() = launch {
        viewModel.uiState.map { it.usedAmount }
            .distinctUntilChanged()
            .collect {
                binding.usedAmountTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeUsedAmountError() = launch {
        viewModel.uiState.map { it.usedAmountError }
            .distinctUntilChanged()
            .collect {
                binding.usedAmountTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeUsedUnit() = launch {
        viewModel.uiState.map { it.usedUnit }
            .distinctUntilChanged()
            .collect { unit ->
                binding.usedUnitPriceTextInputLayout.hint = if (unit != null) {
                    resources.getString(
                        R.string.ffr_add_edit_expense_hint_farm_input_formatted_unit_price,
                        unit.unit
                    )
                } else {
                    resources.getString(R.string.ffr_add_edit_expense_hint_farm_input_unit_price)
                }
                binding.usedUnitAutoCompleteTextView.setText(unit?.unit.orEmpty(), false)
            }
    }

    private fun CoroutineScope.observeUsedUnitError() = launch {
        viewModel.uiState.map { it.usedUnitError }
            .distinctUntilChanged()
            .collect {
                binding.usedUnitDropdownMenu.setError(it)
            }
    }

    private fun CoroutineScope.observeUsedUnitPrice() = launch {
        viewModel.uiState.map { it.usedUnitPrice }
            .distinctUntilChanged()
            .collect {
                binding.usedUnitPriceTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeUsedUnitPriceError() = launch {
        viewModel.uiState.map { it.usedUnitPriceError }
            .distinctUntilChanged()
            .collect {
                binding.usedUnitPriceTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeIsFingerling() = launch {
        viewModel.uiState.map { it.isFingerling }
            .distinctUntilChanged()
            .collect { isFingerling ->
                binding.fingerlingAverageWeightTextInputLayout.isVisible = isFingerling
                binding.fingerlingAverageSizeTextInputLayout.isVisible = isFingerling
                binding.fingerlingAgeTextInputLayout.isVisible = isFingerling
            }
    }

    private fun CoroutineScope.observeFingerlingWeight() = launch {
        viewModel.uiState.map { it.fingerlingWeight }
            .distinctUntilChanged()
            .collect {
                binding.fingerlingAverageWeightTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeFingerlingSize() = launch {
        viewModel.uiState.map { it.fingerlingSize }
            .distinctUntilChanged()
            .collect {
                binding.fingerlingAverageSizeTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeFingerlingAge() = launch {
        viewModel.uiState.map { it.fingerlingAge }
            .distinctUntilChanged()
            .collect {
                binding.fingerlingAgeTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeFingerlingWeightError() = launch {
        viewModel.uiState.map { it.fingerlingWeightError }
            .distinctUntilChanged()
            .collect {
                binding.fingerlingAverageWeightTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeFingerlingSizeError() = launch {
        viewModel.uiState.map { it.fingerlingSizeError }
            .distinctUntilChanged()
            .collect {
                binding.fingerlingAverageSizeTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeFingerlingAgeError() = launch {
        viewModel.uiState.map { it.fingerlingAgeError }
            .distinctUntilChanged()
            .collect {
                binding.fingerlingAgeTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeTotalCost() = launch {
        viewModel.uiState.map { it.totalCost }
            .distinctUntilChanged()
            .collect {
                if (it != null) {
                    binding.totalCostTextInputEdit.bindMoney(it)
                } else {
                    binding.totalCostTextInputEdit.bindText("")
                }
            }
    }

    private fun CoroutineScope.observeTotalCostError() = launch {
        viewModel.uiState.map { it.totalCostError }
            .distinctUntilChanged()
            .collect {
                binding.totalCostTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeInputResult() = launch {
        viewModel.uiState.map { it.inputResult }
            .distinctUntilChanged()
            .collect { result ->
                if (result != null) {
                    setResult(result)
                }
            }
    }

    private fun bindProduct(product: UiFarmInputProduct) {
        binding.toolbarTitleTextView.text = product.name
        bindProductImage(product.thumbnail)
    }

    private fun bindProductImage(thumbnail: String) {
        binding.productThumbnailImageView.load(
            requireContext(),
            thumbnail
        )
    }

    private fun openProductPicker() {
        findNavController().navigate(
            FarmInputInputFragmentDirections.actionFarmInputInputFragmentToFarmInputProductPickerFragment()
        )
    }

    private fun setResult(cost: UiFarmInputCost) {
        setFragmentResult(
            REQUEST_KEY_FARM_INPUT_COST,
            bundleOf(KEY_FARM_INPUT_COST to cost)
        )
        findNavController().popBackStack()
    }


//    private fun showDiscardConfirmDialog() {
//        val dialog = GreenwayAlertDialog(requireContext())
//        dialog.setMessage(R.string.asymt_discard_message)
//        dialog.setOkButtonText(R.string.asymt_discard_cancel_button)
//        dialog.setCancelButtonText(R.string.asymt_discard_ok_button)
//        dialog.setOnPositiveButtonClickListener { dialog.dismiss() }
//        dialog.setOnNegativeButtonClickListener {
//            dialog.dismiss()
//            navigateUpOrFinish(view)
//        }
//        dialog.show()
//    }

    companion object {
        const val REQUEST_KEY_FARM_INPUT_COST = "request_key.FARM_INPUT_COST"
        const val KEY_FARM_INPUT_COST = "key.FARM_INPUT_COST"
    }
}