package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.FfrAddEditFcrRecordFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views.FcrRatioInputItemView.OnFcrRatioInputChangeListener
import greenway_myanmar.org.ui.widget.GreenWayDateInputView
import greenway_myanmar.org.util.extensions.setPaddingBottomForIme
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@AndroidEntryPoint
class AddEditFcrRecordFragment : Fragment(R.layout.ffr_add_edit_fcr_record_fragment) {

    private val binding by viewBinding(FfrAddEditFcrRecordFragmentBinding::bind)

    private val viewModel: AddEditFcrRecordViewModel by viewModels()

    private var unitArrayAdapter: ArrayAdapter<String> by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setPaddingBottomForIme(binding.root)
        setupDateInputUi()
        setupFcrRatiosInputUi()
        setupSubmitButton()
    }

    //
//    private fun setupProductImageUi() {
//        binding.productThumbnailImageView.setOnClickListener {
//            openProductPicker()
//        }
//    }
//
//    private fun setupUsedAmountInputUi() {
//        binding.usedAmountTextInputEditText.doAfterTextChanged {
//            viewModel.handleEvent(
//                FarmInputInputEvent.OnUsedAmountChanged(it?.toString().orEmpty())
//            )
//        }
//    }
//
//    private fun setupUsedUnitDropdownUi() {
//        unitArrayAdapter = ArrayAdapter(
//            requireContext(),
//            R.layout.greenway_dropdown_menu_popup_item,
//            arrayListOf()
//        )
//        binding.usedUnitAutoCompleteTextView.apply {
//            setAdapter(unitArrayAdapter)
//            onItemClickListener =
//                AdapterView.OnItemClickListener { parent, view, position, id ->
//                    viewModel.handleEvent(FarmInputInputEvent.OnUsedUnitSelectionChanged(position))
//                }
//        }
//    }
//
//    private fun setupUsedUnitPriceInputUi() {
//        binding.usedUnitPriceTextInputEditText.doAfterTextChanged {
//            viewModel.handleEvent(
//                FarmInputInputEvent.OnUsedUnitPriceChanged(it?.toString().orEmpty())
//            )
//        }
//    }
//
//    private fun <T> ArrayAdapter<T>.submitList(items: List<T>) {
//        this.clear()
//        this.addAll(items)
//        this.notifyDataSetChanged()
//    }
//
//    //    private fun setupSpeciesInputUi() {
////        binding.speciesTextInputEditText.doAfterTextChanged {
////            viewModel.handleEvent(FishInputUiEvent.OnSpeciesChanged(it?.toString().orEmpty()))
////        }
////    }
////
////    private fun setupFishInputUi() {
////        binding.fishInputEditText.setOnClickListener {
////            viewModel.handleEvent(FishInputUiEvent.ResetFishValidationError)
////            openFishPicker()
////        }
////    }
////
//    private fun setupCancelButton() {
//        binding.cancelButton.setOnClickListener {
//            findNavController().popBackStack()
//        }
//    }
//

    private fun setupDateInputUi() {
        binding.dateInputView.onDateChangeListener = object :
            GreenWayDateInputView.OnDateChangeListener {
            override fun onDateChanged(date: LocalDate) {
                viewModel.handleEvent(AddEditFcrRecordEvent.OnDateChanged(date))
            }
        }
    }

    private fun setupFcrRatiosInputUi() {
        binding.fcrRatioListInputView.setOnInputChangeListener(object :
            OnFcrRatioInputChangeListener {
            override fun onFeedWeightChanged(index: Int, weight: String?) {
                viewModel.handleEvent(AddEditFcrRecordEvent.OnFeedWeightChanged(index, weight))
            }

            override fun onGainWeightChanged(index: Int, weight: String?) {
                viewModel.handleEvent(AddEditFcrRecordEvent.OnGainWeightChanged(index, weight))
            }

            override fun onRatioChanged(index: Int, ratio: BigDecimal?) {
                /* no-op */
            }
        })
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(AddEditFcrRecordEvent.OnSubmit)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeDate()
            observeFishes()
            observeFeedWeights()
            observeGainWeights()
            observeCalculatedRatios()

            observeAllInputErrors()
            observeIndividualInputErrors()

            observeAddEditRecordResult()
        }
    }

    private fun CoroutineScope.observeDate() = launch {
        viewModel.uiState.map { it.date }
            .distinctUntilChanged()
            .collect {
                binding.dateInputView.bindDate(it)
            }
    }

    private fun CoroutineScope.observeFishes() = launch {
        viewModel.uiState.map { it.fishes }
            .distinctUntilChanged()
            .collect {
                binding.fcrRatioListInputView.setFishes(it)
            }
    }

    private fun CoroutineScope.observeFeedWeights() = launch {
        viewModel.uiState.map { it.feedWeights }
            .distinctUntilChanged()
            .collect {
                binding.fcrRatioListInputView.setFeedWeights(it)
            }
    }

    private fun CoroutineScope.observeGainWeights() = launch {
        viewModel.uiState.map { it.gainWeights }
            .distinctUntilChanged()
            .collect {
                binding.fcrRatioListInputView.setGainWeights(it)
            }
    }

    private fun CoroutineScope.observeCalculatedRatios() = launch {
        viewModel.uiState.map { it.calculatedRatios }
            .distinctUntilChanged()
            .collect {
                binding.fcrRatioListInputView.setCalculatedRatios(it)
            }
    }

    private fun CoroutineScope.observeAllInputErrors() = launch {
        viewModel.uiState.map { it.allInputError }
            .distinctUntilChanged()
            .collect { error ->
                if (error != null) {
                    showErrorMessage(error)
                }
            }
    }

    private fun CoroutineScope.observeIndividualInputErrors() = launch {
        viewModel.uiState.map { it.individualInputErrors }
            .distinctUntilChanged()
            .collect { errors ->
               binding.fcrRatioListInputView.setErrors(errors)
            }
    }

    private fun CoroutineScope.observeAddEditRecordResult() = launch {
        viewModel.uiState.map { it.addEditFcrRecordResult }
            .distinctUntilChanged()
            .collect { result ->
                if (result != null) {
                    findNavController().popBackStack()
                }
            }
    }

    private fun showErrorMessage(error: Text) {
        showSnackbar(error)
        viewModel.handleEvent(AddEditFcrRecordEvent.AllInputErrorShown)
    }
//
//    private fun CoroutineScope.observeProduct() = launch {
//        viewModel.uiState.map { it.product }
//            .distinctUntilChanged()
//            .collect { product ->
//                if (product != null) {
//                    bindProduct(product)
//                } else {
//                    openProductPicker()
//                }
//            }
//    }
//
//    private fun CoroutineScope.observeUnits() = launch {
//        viewModel.unitsUiState
//            .collect { uiState ->
//                when (uiState) {
//                    is LoadingState.Success -> {
//                        unitArrayAdapter.submitList(uiState.data.map { it.unit })
//                    }
//                    else -> {
//                        // no-op
//                    }
//                }
//            }
//    }
//
//    private fun CoroutineScope.observeUsedAmount() = launch {
//        viewModel.uiState.map { it.usedAmount }
//            .distinctUntilChanged()
//            .collect {
//                binding.usedAmountTextInputEditText.bindText(it)
//            }
//    }
//
//    private fun CoroutineScope.observeUsedAmountError() = launch {
//        viewModel.uiState.map { it.usedAmountError }
//            .distinctUntilChanged()
//            .collect {
//                binding.usedAmountTextInputLayout.setError(it)
//            }
//    }
//
//    private fun CoroutineScope.observeUsedUnit() = launch {
//        viewModel.uiState.map { it.usedUnit }
//            .distinctUntilChanged()
//            .collect {
//                Timber.d("Used unit: ${it?.unit.orEmpty()}")
//                binding.usedUnitAutoCompleteTextView.setText(it?.unit.orEmpty(), false)
//            }
//    }
//
//    private fun CoroutineScope.observeUsedUnitError() = launch {
//        viewModel.uiState.map { it.usedUnitError }
//            .distinctUntilChanged()
//            .collect {
//                binding.usedUnitDropdownMenu.setError(it)
//            }
//    }
//
//    private fun CoroutineScope.observeUsedUnitPrice() = launch {
//        viewModel.uiState.map { it.usedUnitPrice }
//            .distinctUntilChanged()
//            .collect {
//                binding.usedUnitPriceTextInputEditText.bindText(it)
//            }
//    }
//
//    private fun CoroutineScope.observeUsedUnitPriceError() = launch {
//        viewModel.uiState.map { it.usedUnitPriceError }
//            .distinctUntilChanged()
//            .collect {
//                binding.usedUnitPriceTextInputLayout.setError(it)
//            }
//    }
//
//    private fun CoroutineScope.observeTotalCost() = launch {
//        viewModel.uiState.map { it.totalCost }
//            .distinctUntilChanged()
//            .collect {
//                if (it != null) {
//                    binding.totalCostTextInputEdit.bindMoney(it)
//                } else {
//                    binding.totalCostTextInputEdit.bindText("")
//                }
//            }
//    }
//
//    private fun CoroutineScope.observeTotalCostError() = launch {
//        viewModel.uiState.map { it.totalCostError }
//            .distinctUntilChanged()
//            .collect {
//                binding.totalCostTextInputLayout.setError(it)
//            }
//    }
//
//    private fun bindProduct(product: UiFarmInputProduct) {
//        binding.toolbarTitleTextView.setText(product.name)
//        bindProductImage(product.thumbnail)
//    }
//
//    private fun bindProductImage(thumbnail: String) {
//        binding.productThumbnailImageView.load(
//            requireContext(),
//            thumbnail
//        )
//    }
//
//    private fun openProductPicker() {
//
//    }


}