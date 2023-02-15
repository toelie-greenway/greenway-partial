package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.cropincomelist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrCropIncomeListFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmDetailViewModel
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CropIncomeListFragment : Fragment(R.layout.ffr_crop_income_list_fragment) {

    private val binding by viewBinding(FfrCropIncomeListFragmentBinding::bind)

    private var adapter: CropIncomeListAdapter by autoCleared()

    private val viewModel: CropIncomeListViewModel by viewModels()
    private val parentViewModel: FarmDetailViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupList()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeCropIncomes()
            observeFarmUiStateFromParentViewModel()
        }
    }

    private fun setupList() {
        adapter = CropIncomeListAdapter()
        binding.list.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 16))
        binding.list.adapter = adapter
    }

    private fun CoroutineScope.observeCropIncomes() = launch {
        viewModel.cropIncomesUiState
            .collect { uiState ->
                if (uiState is LoadingState.Success) {
                    adapter.submitList(uiState.data)
                }
                binding.loadingStateView.bind(uiState)
            }
    }

    private fun CoroutineScope.observeFarmUiStateFromParentViewModel() = launch {
        parentViewModel.farmUiState
            .collect { farmUiState ->
                viewModel.handleEvent(CropIncomeListEvent.OnFarmChanged(farmUiState))
            }
    }
}