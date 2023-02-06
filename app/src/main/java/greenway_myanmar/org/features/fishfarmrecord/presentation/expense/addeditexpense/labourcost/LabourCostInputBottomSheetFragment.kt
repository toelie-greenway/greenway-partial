package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.labourcost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FfrLabourCostInputBottomSheetFragmentBinding
import greenway_myanmar.org.util.extensions.bindMoney
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LabourCostInputBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding by autoCleared<FfrLabourCostInputBottomSheetFragmentBinding>()

    val viewModel by viewModels<LabourCostInputViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FfrLabourCostInputBottomSheetFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        (dialog as BottomSheetDialog?)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupUi() {
        setupLabourQuantityUi()
        setupLabourCostUi()
        setupFamilyMemberQuantityUi()
        setupFamilyMemberCostUi()
        setupSubmitButton()
    }

    private fun setupLabourQuantityUi() {
        binding.labourQuantityTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                LabourCostInputEvent.OnLabourQuantityChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupLabourCostUi() {
        binding.labourCostTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                LabourCostInputEvent.OnLabourCostChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupFamilyMemberQuantityUi() {
        binding.familyMemberQuantityTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                LabourCostInputEvent.OnFamilyMemberQuantityChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupFamilyMemberCostUi() {
        binding.familyMemberCostTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                LabourCostInputEvent.OnFamilyMemberCostChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(LabourCostInputEvent.OnSubmit)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeLabourQuantity()
            observeLabourQuantityError()
            observeLabourCost()
            observeLabourCostError()
            observeFamilyMemberQuantity()
            observeFamilyMemberCost()
            observeTotalCost()
            observeInputResult()
        }
    }

    private fun CoroutineScope.observeLabourQuantity() = launch {
        viewModel.uiState.map { it.labourQuantity }
            .distinctUntilChanged()
            .collect {
                binding.labourQuantityTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeLabourQuantityError() = launch {
        viewModel.uiState.map { it.labourQuantityError }
            .distinctUntilChanged()
            .collect {
                binding.labourQuantityTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeLabourCost() = launch {
        viewModel.uiState.map { it.labourCost }
            .distinctUntilChanged()
            .collect {
                binding.labourCostTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeLabourCostError() = launch {
        viewModel.uiState.map { it.labourCostError }
            .distinctUntilChanged()
            .collect {
                binding.labourCostTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeFamilyMemberQuantity() = launch {
        viewModel.uiState.map { it.familyMemberQuantity }
            .distinctUntilChanged()
            .collect {
                binding.familyMemberQuantityTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeFamilyMemberCost() = launch {
        viewModel.uiState.map { it.familyMemberCost }
            .distinctUntilChanged()
            .collect {
                binding.familyMemberCostTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeTotalCost() = launch {
        viewModel.uiState.map { it.totalCost }
            .distinctUntilChanged()
            .collect {
                binding.totalCostTextInputEditText.bindMoney(it)
            }
    }

    private fun CoroutineScope.observeInputResult() = launch {
        viewModel.uiState.map { it.inputResult }
            .distinctUntilChanged()
            .collect { result ->
                if (result != null) {
                    setFragmentResult(REQUEST_KEY_LABOUR_COST, bundleOf(KEY_LABOUR_COST to result))
                    findNavController().popBackStack()
                }
            }
    }
//
//
//    private fun initNavigationEvents() {
//        viewModel.submitEvent.observe(viewLifecycleOwner, Observer { doSubmit() })
//    }
//
//    private fun doSubmit() {
//        val labourQuantity = binding.labourQuantityTextInputView.getValue().toIntOrNull()
//        val labourCost = binding.labourCostTextInputView.getValue().toIntOrNull()
//        val animalQuantity = binding.animalQuantityTextInputView.getValue().toIntOrNull()
//        val animalCost = binding.animalCostTextInputView.getValue().toIntOrNull()
//        val familyMemberQuantity =
//            binding.familyMemberQuantityTextInputView.getValue().toIntOrNull()
//        val familyMemberCost = binding.familyMemberCostTextInputView.getValue().toIntOrNull()
//
//        binding.labourQuantityTextInputView.clearError()
//        binding.labourCostTextInputView.clearError()
//        binding.animalQuantityTextInputView.clearError()
//        binding.animalCostTextInputView.clearError()
//
//        if ((labourQuantity == null) xor (labourCost == null)) {
//            binding.labourQuantityTextInputView.validate()
//            binding.labourCostTextInputView.validate()
//            return
//        }
//
//        if ((animalQuantity == null) xor (animalCost == null)) {
//            binding.animalQuantityTextInputView.validate()
//            binding.animalCostTextInputView.validate()
//            return
//        }
//
//        val cost =
//            AsymtLabourCost(
//                labourQuantity ?: 0,
//                labourCost ?: 0,
//                animalQuantity ?: 0,
//                animalCost ?: 0,
//                familyMemberQuantity ?: 0,
//                familyMemberCost ?: 0
//            )
//
//        setFragmentResult(REQUEST_KEY_LABOUR_COST, bundleOf(KEY_LABOUR_COST to cost))
//        findNavController().popBackStack()
//    }
//
//    private fun addTextChangeListener() {
//        binding.labourQuantityTextInputView.getEditText()
//            ?.addTextChangedListener(textChangeListener)
//        binding.labourCostTextInputView.getEditText()?.addTextChangedListener(textChangeListener)
//        binding.animalQuantityTextInputView.getEditText()
//            ?.addTextChangedListener(textChangeListener)
//        binding.animalCostTextInputView.getEditText()?.addTextChangedListener(textChangeListener)
//        binding
//            .familyMemberQuantityTextInputView
//            .getEditText()
//            ?.addTextChangedListener(textChangeListener)
//        binding.familyMemberCostTextInputView.getEditText()
//            ?.addTextChangedListener(textChangeListener)
//    }
//
//    private fun initForm() {
//        //        form.add(
//        //                AsymtLabourCost.KEY_LABOUR_QTY to binding.
//        //        )
//    }
//
//    private val textChangeListener =
//        object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                displayTotal()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        }
//
//    private fun displayTotal() {
//        val labourQuantity = binding.labourQuantityTextInputView.getValue().toIntOrNull()
//        val labourCost = binding.labourCostTextInputView.getValue().toIntOrNull()
//        val animalQuantity = binding.animalQuantityTextInputView.getValue().toIntOrNull()
//        val animalCost = binding.animalCostTextInputView.getValue().toIntOrNull()
//        val familyMemberQuantity =
//            binding.familyMemberQuantityTextInputView.getValue().toIntOrNull()
//        val familyMemberCost = binding.familyMemberCostTextInputView.getValue().toIntOrNull()
//
//        var total = 0
//
//        if (labourQuantity != null && labourCost != null) {
//            total += labourCost
//        }
//        if (animalQuantity != null && animalCost != null) {
//            total += animalCost
//        }
//        binding.total = total
//    }

    companion object {
        const val REQUEST_KEY_LABOUR_COST = "request_key.LABOUR_COST"
        const val KEY_LABOUR_COST = "key.LABOUR_COST"
    }
}
