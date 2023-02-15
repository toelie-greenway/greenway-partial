package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.core.presentation.util.numberFormat
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.FfrAddEditProductionRecordFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord.views.ProductionListInputView
import greenway_myanmar.org.ui.widget.GreenWayDateInputView
import greenway_myanmar.org.util.extensions.setPaddingBottomForIme
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class AddEditProductionRecordFragment : Fragment(R.layout.ffr_add_edit_production_record_fragment) {

    private val binding by viewBinding(FfrAddEditProductionRecordFragmentBinding::bind)

    private val viewModel: AddEditProductionRecordViewModel by viewModels()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setPaddingBottomForIme(binding.root)
        setupToolbar()
        setupDateInputUi()
        setupProductionInputUi()
        setupSubmitButton()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeDate()

            observeFishesAndWeights()
            observeWeights()
            observePrices()

            observeSubtotalWeight()
            observeSubtotalPrice()

            observeTotalWeight()
            observeTotalPrice()
            observeAllInputErrors()
            observeProductionRecordSavingState()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupDateInputUi() {
        binding.dateInputView.onDateChangeListener = object :
            GreenWayDateInputView.OnDateChangeListener {
            override fun onDateChanged(date: LocalDate) {
                viewModel.handleEvent(AddEditProductionRecordEvent.OnDateChanged(date))
            }
        }
    }

    private fun setupProductionInputUi() {
        binding.productionListInputView.setOnProductionInputChangeListener(object :
            ProductionListInputView.OnProductionInputChangeListener {
            override fun onWeightChanged(fishId: String, size: UiFishSize, weight: String?) {
                viewModel.handleEvent(
                    AddEditProductionRecordEvent.OnWeightChanged(
                        fishId = fishId,
                        size = size,
                        weight = weight
                    )
                )
            }

            override fun onPriceChanged(fishId: String, size: UiFishSize, price: String?) {
                viewModel.handleEvent(
                    AddEditProductionRecordEvent.OnPriceChanged(
                        fishId = fishId,
                        size = size,
                        price = price
                    )
                )

            }
        })
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(AddEditProductionRecordEvent.OnSubmit)
        }
    }

    private fun CoroutineScope.observeDate() = launch {
        viewModel.uiState.map { it.date }
            .distinctUntilChanged()
            .collect {
                binding.dateInputView.bindDate(it)
            }
    }

    private fun CoroutineScope.observeFishesAndWeights() = launch {
        combine(
            viewModel.uiState.mapNotNull { it.fishes }.distinctUntilChanged(),
            viewModel.uiState.mapNotNull { it.fishSizes }.distinctUntilChanged(),
            ::Pair
        )
            .distinctUntilChanged()
            .collect { (fishes, fishSizes) ->
                binding.productionListInputView.setFishesAndSizes(
                    fishes,
                    fishSizes
                )
            }
    }

    private fun CoroutineScope.observeWeights() = launch {
        viewModel.uiState.map { it.weightsByFishAndSize }
            .distinctUntilChanged()
            .collect {
                binding.productionListInputView.setWeights(it)
            }
    }

    private fun CoroutineScope.observePrices() = launch {
        viewModel.uiState.map { it.pricesByFishAndSize }
            .distinctUntilChanged()
            .collect {
                binding.productionListInputView.setPrices(it)
            }
    }

    private fun CoroutineScope.observeSubtotalWeight() = launch {
        viewModel.uiState.map { it.subtotalWeightByFish }
            .distinctUntilChanged()
            .collect {
                binding.productionListInputView.setSubtotalWeight(it)
            }
    }

    private fun CoroutineScope.observeSubtotalPrice() = launch {
        viewModel.uiState.map { it.subtotalPriceByFish }
            .distinctUntilChanged()
            .collect {
                binding.productionListInputView.setSubtotalPrice(it)
            }
    }

    private fun CoroutineScope.observeTotalWeight() = launch {
        viewModel.uiState.map { it.totalWeight }
            .distinctUntilChanged()
            .collect {
                binding.totalWeightTextView.text =
                    resources.getString(R.string.formatted_viss, numberFormat.format(it))
            }
    }

    private fun CoroutineScope.observeTotalPrice() = launch {
        viewModel.uiState.map { it.totalPrice }
            .distinctUntilChanged()
            .collect {
                binding.totalPriceTextView.text =
                    resources.getString(R.string.format_kyat, numberFormat.format(it))
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


    private fun CoroutineScope.observeProductionRecordSavingState() = launch {
        viewModel.uiState.map { it.productionRecordSavingState }
            .distinctUntilChanged()
            .collect { state ->
                binding.savingStateContainer.isVisible = state is LoadingState.Loading
                if (state is LoadingState.Success) {
                    navController.popBackStack()
                } else if (state is LoadingState.Error && state.message != null) {
                    showSavingProductionRecordError(state.message)
                }
            }
    }

    private fun showErrorMessage(error: Text) {
        showSnackbar(error)
        viewModel.handleEvent(AddEditProductionRecordEvent.AllInputErrorShown)
    }

    private fun showSavingProductionRecordError(message: Text) {
        showSnackbar(message)
        viewModel.handleEvent(AddEditProductionRecordEvent.OnSavingProductionRecordErrorShown)
    }


}