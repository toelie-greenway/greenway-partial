package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.machinerycost

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import greenway_myanmar.org.databinding.FfrMachineryCostInputBottomSheetFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import greenway_myanmar.org.ui.common.OnKeyboardVisibilityListener
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MachineryCostInputBottomSheetFragment : BottomSheetDialogFragment(),
    OnKeyboardVisibilityListener {

    private var binding by autoCleared<FfrMachineryCostInputBottomSheetFragmentBinding>()

    val viewModel by viewModels<MachineryCostInputViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FfrMachineryCostInputBottomSheetFragmentBinding.inflate(
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
        (dialog as BottomSheetDialog?)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupUi() {
        setupMachineryCostInputUi()
        setupSubmitButton()
    }

    private fun setupMachineryCostInputUi() {
        binding.machineCostTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                MachineryCostInputEvent.OnMachineryCostChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(
                MachineryCostInputEvent.OnSubmit
            )
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeMachineryCost()
            observeMachineryCostError()
            observeInputResult()
        }
    }

    private fun CoroutineScope.observeMachineryCost() = launch {
        viewModel.uiState.map { it.machineCost }
            .distinctUntilChanged()
            .collect {
                binding.machineCostTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeMachineryCostError() = launch {
        viewModel.uiState.map { it.machineCostError }
            .distinctUntilChanged()
            .collect {
                binding.machineCostTextInputLayout.setError(it)
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

    private fun setResult(machineryCost: UiMachineryCost) {
        setFragmentResult(
            REQUEST_KEY_MACHINERY_COST,
            bundleOf(KEY_MACHINERY_COST to machineryCost)
        )
        findNavController().popBackStack()
    }

    private fun setKeyboardVisibilityListener(
        onKeyboardVisibilityListener: OnKeyboardVisibilityListener
    ) {
        val parentView =
            (requireActivity().findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                private var alreadyOpen = false
                private val defaultKeyboardHeightDP = 100
                private val EstimatedKeyboardDP =
                    defaultKeyboardHeightDP +
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
                private val rect = Rect()

                override fun onGlobalLayout() {
                    val estimatedKeyboardHeight =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            EstimatedKeyboardDP.toFloat(),
                            parentView.resources.displayMetrics
                        )
                    parentView.getWindowVisibleDisplayFrame(rect)
                    val heightDiff: Int = parentView.rootView.height - (rect.bottom - rect.top)
                    val isShown = heightDiff >= estimatedKeyboardHeight
                    if (isShown == alreadyOpen) {
                        Timber.d("Keyboard state", "Ignoring global layout change...")
                        return
                    }
                    alreadyOpen = isShown
                    onKeyboardVisibilityListener.onVisibilityChanged(isShown)
                }
            }
        )
    }

    override fun onVisibilityChanged(visible: Boolean) {
        val baseDialog = dialog
        if (baseDialog is BottomSheetDialog) {
            val behavior: BottomSheetBehavior<*> = baseDialog.behavior
            if (visible) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }

    companion object {
        const val REQUEST_KEY_MACHINERY_COST = "request_key.MACHINERY_COST"
        const val KEY_MACHINERY_COST = "key.MACHINERY_COST"

        fun newInstance(): MachineryCostInputBottomSheetFragment {
            return MachineryCostInputBottomSheetFragment()
        }
    }
}
