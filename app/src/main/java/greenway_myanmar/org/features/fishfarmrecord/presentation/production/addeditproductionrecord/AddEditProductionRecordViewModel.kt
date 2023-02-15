package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFish
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionPerFishSize
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asDomainModel
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinInstant
import timber.log.Timber
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class AddEditProductionRecordViewModel @Inject constructor(
    private val saveProductionRecordUseCase: SaveProductionRecordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = AddEditProductionRecordFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val _uiState = MutableStateFlow(AddEditProductionRecordUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: AddEditProductionRecordUiState
        get() = uiState.value

    init {
        _uiState.update {
            it.copy(
                fishes = args.fishes.toList(),
                fishSizes = UiFishSize.values().toList()
            )
        }
        observePriceAndSizeChanges()
        observeSubtotalsChanges()
    }

    private fun observePriceAndSizeChanges() {
        viewModelScope.launch {
            combine(
                uiState.map { it.weightsByFishAndSize }.distinctUntilChanged(),
                uiState.map { it.pricesByFishAndSize }.distinctUntilChanged(),
                ::Pair
            ).collect { (weightsByFishAndSize, pricesByFishAndSize) ->
                val weightsByFish = mutableMapOf<String, BigDecimal>()
                for ((fishAndSize, weight) in weightsByFishAndSize) {
                    val (fishId, size) = fishAndSize
                    weightsByFish[fishId] =
                        weightsByFish[fishId].orZero().plus(weight.toBigDecimalOrZero())
                }

                val pricesByFish = mutableMapOf<String, BigDecimal>()
                for ((fishAndSize, weight) in pricesByFishAndSize) {
                    val (fishId, size) = fishAndSize
                    pricesByFish[fishId] =
                        pricesByFish[fishId].orZero().plus(weight.toBigDecimalOrZero())
                }
                _uiState.update {
                    it.copy(
                        subtotalPriceByFish = pricesByFish,
                        subtotalWeightByFish = weightsByFish
                    )
                }
            }
        }
    }

    private fun observeSubtotalsChanges() {
        viewModelScope.launch {
            combine(
                uiState.map { it.subtotalWeightByFish }.distinctUntilChanged(),
                uiState.map { it.subtotalPriceByFish }.distinctUntilChanged(),
                ::Pair
            ).collect { (subtotalWeightByFish, subtotalPriceByFish) ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        totalWeight = subtotalWeightByFish.values.sumOf { it },
                        totalPrice = subtotalPriceByFish.values.sumOf { it },
                    )
                }
            }
        }
    }

    fun handleEvent(event: AddEditProductionRecordEvent) {
        when (event) {
            is AddEditProductionRecordEvent.OnDateChanged -> {
                updateDate(event.date)
            }
            is AddEditProductionRecordEvent.OnWeightChanged -> {
                updateWeight(event.fishId, event.size, event.weight)
            }
            is AddEditProductionRecordEvent.OnPriceChanged -> {
                updatePrice(event.fishId, event.size, event.price)
            }
            AddEditProductionRecordEvent.OnSubmit -> {
                onSubmit()
            }
            AddEditProductionRecordEvent.AllInputErrorShown -> {
                clearAllInputError()
            }
            AddEditProductionRecordEvent.OnSavingProductionRecordErrorShown -> {
                clearSavingProductionRecordError()
            }
        }
    }

    private fun updateDate(date: LocalDate) {
        _uiState.value = currentUiState.copy(date = date)
    }

    private fun clearAllInputError() {
        _uiState.update {
            it.copy(allInputError = null)
        }
    }

    private fun clearSavingProductionRecordError() {
        _uiState.update {
            it.copy(productionRecordSavingError = null)
        }
    }

    private fun updateWeight(fishId: String, size: UiFishSize, weight: String?) {
        val currentWeight = currentUiState.weightsByFishAndSize[Pair(fishId, size)]
        if (currentWeight == weight) {
            return
        }

        val newWeights = currentUiState.weightsByFishAndSize.toMutableMap()
        newWeights[Pair(fishId, size)] = weight
        _uiState.update {
            it.copy(weightsByFishAndSize = newWeights)
        }
    }

    private fun updatePrice(fishId: String, size: UiFishSize, price: String?) {
        val currentPrice = currentUiState.pricesByFishAndSize[Pair(fishId, size)]
        if (currentPrice == price) {
            return
        }

        val newPrices = currentUiState.pricesByFishAndSize.toMutableMap()
        newPrices[Pair(fishId, size)] = price
        _uiState.update {
            it.copy(pricesByFishAndSize = newPrices)
        }
    }

    private fun onSubmit() {
        // validate inputs
        val dateValidationResult = validateDate(currentUiState.date)
        val allInputValidationResult =
            validateAllInput(currentUiState.totalWeight, currentUiState.totalPrice)

        // set/reset error
        _uiState.value = currentUiState.copy(
            dateError = dateValidationResult.getErrorOrNull(),
            allInputError = allInputValidationResult.getErrorOrNull()
        )

        // return if there is any error
        if (hasError(
                dateValidationResult,
                allInputValidationResult
            )
        ) {
            return
        }

        // collect result
        val date = dateValidationResult.getDataOrThrow()

        Timber.d("Date: $date")
        for ((fishAndSize, weight) in currentUiState.weightsByFishAndSize) {
            val (fishId, size) = fishAndSize
            Timber.d("Weight=$weight, FishId=$fishId, Size=$size")
        }
        for ((fishAndSize, price) in currentUiState.pricesByFishAndSize) {
            val (fishId, size) = fishAndSize
            Timber.d("Price=$price, FishId=$fishId, Size=$size")
        }

        val productionsPerFish = collectProductionsPerFish()
        productionsPerFish.forEach { productionPerFish ->
            Timber.d("=== FishId: ${productionPerFish.fish.id} ===")
            Timber.d("Fish = ${productionPerFish.fish}")
            Timber.d("ProductionsPerFishSize = ${productionPerFish.productionsPerFishSize}")
        }

        val cleanedProductionsPerFish = productionsPerFish.removeIfEmpty()
        cleanedProductionsPerFish.forEach { productionPerFish ->
            Timber.d("=== FishId: ${productionPerFish.fish.id} After Remove ===")
            Timber.d("Fish = ${productionPerFish.fish}")
            Timber.d("ProductionsPerFishSize = ${productionPerFish.productionsPerFishSize}")
        }

        val request = SaveProductionRecordRequest(
            id = null,
            date = date.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toKotlinInstant(),
            productionsPerFish = cleanedProductionsPerFish.map {
                ProductionPerFish(
                    fish = it.fish,
                    productionsPerFishSize = it.productionsPerFishSize
                )
            },
            seasonId = args.seasonId
        )
        saveProductionRecord(request)
    }

    private fun saveProductionRecord(request: SaveProductionRecordRequest) {
        viewModelScope.launch {
            viewModelScope.launch {
                runCancellableCatching {
                    _uiState.update {
                        it.copy(
                            productionRecordSavingState = LoadingState.Loading
                        )
                    }
                    saveProductionRecordUseCase(request)
                }.onSuccess {
                    _uiState.update {
                        it.copy(
                            productionRecordSavingState = LoadingState.Success(Unit)
                        )
                    }
                }.onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            productionRecordSavingState = LoadingState.Error(exception)
                        )
                    }
                }
            }

        }
    }

    private fun collectProductionsPerFish(): MutableList<ProductionPerFish> {
        val productionsByFishAndSize =
            mutableMapOf<Pair<String, UiFishSize>, ProductionPerFishSize>()
        currentUiState.fishes.orEmpty().forEach { fish ->
            currentUiState.fishSizes.orEmpty().forEach { fishSize ->
                val keyPair = Pair(fish.id, fishSize)
                productionsByFishAndSize[keyPair] = ProductionPerFishSize(
                    fishSize = fishSize.asDomainModel(),
                    weight = currentUiState.weightsByFishAndSize[keyPair]
                        .toBigDecimalOrZero(),
                    price = currentUiState.pricesByFishAndSize[keyPair]
                        .toBigDecimalOrZero(),
                )
            }
        }

        val productionsByFish =
            mutableMapOf<String, MutableList<ProductionPerFishSize>>()
        for ((fishAndSize, productionPerFishSize) in productionsByFishAndSize) {
            val (fishId, size) = fishAndSize
            val productionsPerFishSize = productionsByFish[fishId] ?: mutableListOf()
            productionsPerFishSize.add(productionPerFishSize)
            productionsByFish[fishId] = productionsPerFishSize
        }

        val productionsPerFish = mutableListOf<ProductionPerFish>()
        for ((fishId, productionsPerFishSize) in productionsByFish) {
            productionsPerFish.add(
                ProductionPerFish(
                    fish = currentUiState.fishes?.find { it.id == fishId }?.asDomainModel()
                        ?: throw IllegalStateException("Fish with id $fishId should exist!"),
                    productionsPerFishSize = productionsPerFishSize
                )
            )
        }
        return productionsPerFish
    }

    @JvmName("removeProductionsPerFishIfEmpty")
    private fun List<ProductionPerFish>.removeIfEmpty(): List<ProductionPerFish> {
        return this.map {
            it.copy(
                fish = it.fish,
                productionsPerFishSize = it.productionsPerFishSize.removeIfEmpty()
            )
        }
    }

    @JvmName("removeProductionsPerFishSizeIfEmpty")
    private fun List<ProductionPerFishSize>.removeIfEmpty(): List<ProductionPerFishSize> {
        return this.filterNot { it.price <= BigDecimal.ZERO && it.weight <= BigDecimal.ZERO }
    }

    private fun validateDate(date: LocalDate?): ValidationResult<LocalDate> {
        return if (date == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(date)
        }
    }

    private fun validateAllInput(
        totalWeight: BigDecimal,
        totalPrice: BigDecimal
    ): ValidationResult<Unit> {
        return if (totalPrice <= BigDecimal.ZERO && totalWeight <= BigDecimal.ZERO) {
            ValidationResult.Error(Text.ResourceText(R.string.ffr_add_edit_production_error_fields_required))
        } else {
            ValidationResult.Success(Unit)
        }
    }
}