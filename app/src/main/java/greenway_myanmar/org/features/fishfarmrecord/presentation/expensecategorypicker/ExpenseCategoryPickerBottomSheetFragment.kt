package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.common.decoration.GreenWayDividerItemDecoration
import greenway_myanmar.org.databinding.FfrExpenseCategoryPickerBottomSheetFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseCategoryPickerBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding by autoCleared<FfrExpenseCategoryPickerBottomSheetFragmentBinding>()

    private var adapter by autoCleared<ExpenseCategoryPickerAdapter>()

    val viewModel by viewModels<ExpenseCategoryPickerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FfrExpenseCategoryPickerBottomSheetFragmentBinding.inflate(
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

    private fun setupUi() {
        setupList()
        setupLoadingStateUi()
    }

    private fun setupList() {
        adapter =
            ExpenseCategoryPickerAdapter(object : ExpenseCategoryPickerAdapter.ItemClickCallback {
                override fun onItemClicked(category: UiExpenseCategory) {
                    onCategorySelected(category)
                }
            })
        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            GreenWayDividerItemDecoration.Builder(requireContext()).hideLastDivider(true).build()
        )
    }

    private fun setupLoadingStateUi() {
        binding.loadingStateView.setRetryCallback {
            viewModel.handleEvent(ExpenseCategoryPickerEvent.RetryLoadingCategories)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeCategories()
        }
    }

    private fun CoroutineScope.observeCategories() = launch {
        viewModel.expenseCategoryListUiState.collect { uiState ->
            if (uiState is LoadingState.Success) {
                adapter.submitList(uiState.data)
            }
            binding.loadingStateView.bind(uiState)
        }
    }

    override fun onStart() {
        super.onStart()
        expandBottomSheet()
    }

    private fun expandBottomSheet() {
        (dialog as BottomSheetDialog?)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun onCategorySelected(category: UiExpenseCategory) {
        viewModel.handleEvent(
            ExpenseCategoryPickerEvent.ToggleCategorySelection(category.id)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            delay(150)
            setResult(category)
        }
    }

    private fun setResult(category: UiExpenseCategory) {
        setFragmentResult(
            REQUEST_KEY_EXPENSE_CATEGORY,
            bundleOf(KEY_EXPENSE_CATEGORY to category)
        )
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY_EXPENSE_CATEGORY = "key.EXPENSE_CATEGORY"
        const val KEY_EXPENSE_CATEGORY = "key.EXPENSE_CATEGORY"

        fun newInstance(): ExpenseCategoryPickerBottomSheetFragment {
            return ExpenseCategoryPickerBottomSheetFragment()
        }
    }
}