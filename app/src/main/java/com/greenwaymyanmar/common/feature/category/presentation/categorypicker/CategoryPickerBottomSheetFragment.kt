package com.greenwaymyanmar.common.feature.category.presentation.categorypicker

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
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.common.decoration.GreenWayDividerItemDecoration
import greenway_myanmar.org.databinding.CategoryPickerBottomSheetFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryPickerBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding by autoCleared<CategoryPickerBottomSheetFragmentBinding>()

    private var adapter by autoCleared<CategoryPickerAdapter>()

    val viewModel by viewModels<CategoryPickerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CategoryPickerBottomSheetFragmentBinding.inflate(inflater, container, false)
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
        setupCloseUi()
    }

    private fun setupList() {
        adapter =
            CategoryPickerAdapter(
                object : CategoryPickerAdapter.ItemClickCallback {
                    override fun onItemClicked(category: UiCategory) {
                        onCategorySelected(category)
                    }
                }
            )
        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            GreenWayDividerItemDecoration.Builder(requireContext()).hideLastDivider(true).build()
        )
    }

    private fun setupLoadingStateUi() {
        binding.loadingStateView.setRetryCallback {
            viewModel.handleEvent(CategoryPickerEvent.RetryLoadingCategories)
        }
    }

    private fun setupCloseUi() {
        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle { observeCategories() }
    }

    private fun CoroutineScope.observeCategories() = launch {
        viewModel.categories.collect { uiState ->
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

    private fun onCategorySelected(category: UiCategory) {
        viewModel.handleEvent(CategoryPickerEvent.ToggleCategorySelection(category.id))

        viewLifecycleOwner.lifecycleScope.launch {
            delay(150)
            setResult(category)
        }
    }

    private fun setResult(category: UiCategory) {
        setFragmentResult(REQUEST_KEY_CATEGORY, bundleOf(KEY_CATEGORY to category))
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY_CATEGORY = "key.CATEGORY"
        const val KEY_CATEGORY = "key.CATEGORY"

        fun newInstance(): CategoryPickerBottomSheetFragment {
            return CategoryPickerBottomSheetFragment()
        }
    }
}
