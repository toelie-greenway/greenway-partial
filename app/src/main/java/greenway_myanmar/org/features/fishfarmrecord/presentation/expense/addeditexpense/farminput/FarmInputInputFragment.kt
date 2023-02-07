package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFarmInputInputFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products.FarmInputProductPickerFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.util.extensions.bindMoney
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FarmInputInputFragment : Fragment(R.layout.ffr_farm_input_input_fragment) {

    private val viewModel: FarmInputInputViewModel by viewModels()

    private val binding by viewBinding(FfrFarmInputInputFragmentBinding::bind)

    private var unitArrayAdapter: ArrayAdapter<String> by autoCleared()

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
        unitArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.greenway_dropdown_menu_popup_item,
            arrayListOf()
        )
        binding.usedUnitAutoCompleteTextView.apply {
            setAdapter(unitArrayAdapter)
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

    private fun <T> ArrayAdapter<T>.submitList(items: List<T>) {
        this.clear()
        this.addAll(items)
        this.notifyDataSetChanged()
    }

    //    private fun setupSpeciesInputUi() {
//        binding.speciesTextInputEditText.doAfterTextChanged {
//            viewModel.handleEvent(FishInputUiEvent.OnSpeciesChanged(it?.toString().orEmpty()))
//        }
//    }
//
//    private fun setupFishInputUi() {
//        binding.fishInputEditText.setOnClickListener {
//            viewModel.handleEvent(FishInputUiEvent.ResetFishValidationError)
//            openFishPicker()
//        }
//    }
//
    private fun setupCancelButton() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
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
            observeTotalCostError()
//            observeFishError()
//            observeSpecies()
//            observeSubmitted()
//            observeNavigationEvents()
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
                        unitArrayAdapter.submitList(uiState.data.map { it.unit })
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
            .collect {
                Timber.d("Used unit: ${it?.unit.orEmpty()}")
                binding.usedUnitAutoCompleteTextView.setText(it?.unit.orEmpty(), false)
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

    //
//    private fun CoroutineScope.observeNavigationEvents() = launch {
//        viewModel.navigationEvents.collect { event ->
//            when (event) {
//                is FishInputNavigationEvent.NavigateBackWithResult -> {
//                    navigateBackWithResult(event.fish)
//                }
//            }
//        }
//    }
//
//    private fun CoroutineScope.observeFishError() = launch {
//        viewModel.uiState.map { it.fishValidationError }
//            .distinctUntilChanged()
//            .collect {
//                binding.fishInputLayout.setError(it)
//            }
//    }
//
//    private fun CoroutineScope.observeSubmitted() = launch {
//        viewModel.uiState.map { it.submitted }
//            .distinctUntilChanged()
//            .collect {
//
//            }
//    }
//
    private fun bindProduct(product: UiFarmInputProduct) {
        binding.toolbarTitleTextView.setText(product.name)
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
}