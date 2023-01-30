package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.loanduration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FfrCustomLoanDurationInputBottomSheetFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.loanduration.CustomLoanDurationUiState.CustomLoanDurationInputResult
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.extensions.setNavigationResult
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomLoadDurationInputBottomSheetFragment : BottomSheetDialogFragment(){

    private var binding: FfrCustomLoanDurationInputBottomSheetFragmentBinding by autoCleared()

    val viewModel: CustomLoadDurationInputViewModel by viewModels()

    private val args : CustomLoadDurationInputBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FfrCustomLoanDurationInputBottomSheetFragmentBinding.inflate(
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
        setArgMonth()
    }

    private fun setArgMonth() {
        if (args.month > 0) {
            viewModel.handleEvent(
                CustomLoadDurationEvent.OnMonthChanged(args.month.toString())
            )
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog as BottomSheetDialog?)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupUi() {
        setupMonthInputUi()
        setupSubmitButton()
    }

    private fun setupMonthInputUi() {
        binding.loanDurationMonthTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                CustomLoadDurationEvent.OnMonthChanged(it?.toString())
            )
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(
                CustomLoadDurationEvent.OnSubmit
            )
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeMonth()
            observeMonthError()
            observeInputResult()
        }
    }

    private fun CoroutineScope.observeMonth() = launch {
        viewModel.uiState.map { it.month }
            .distinctUntilChanged()
            .collect {
                binding.loanDurationMonthTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeMonthError() = launch {
        viewModel.uiState.map { it.monthError }
            .distinctUntilChanged()
            .collect {
                binding.loanDurationMonthTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeInputResult() = launch {
        viewModel.uiState.map { it.inputResult }
            .distinctUntilChanged()
            .collect {
                if (it != null) {
                    setResult(it)
                }
            }
    }

    private fun setResult(result: CustomLoanDurationInputResult) {
        setNavigationResult(KEY_LOAN_DURATION_MONTH, result.month)
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY_LOAN_DURATION_MONTH = "request_key.LOAN_DURATION_MONTH"
        const val KEY_LOAN_DURATION_MONTH = "key.LOAN_DURATION_MONTH"
    }
}
