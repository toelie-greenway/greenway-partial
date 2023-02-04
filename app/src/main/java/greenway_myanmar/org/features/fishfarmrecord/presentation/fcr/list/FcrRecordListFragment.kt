package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrFcrRecordListFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FcrRecordListFragment : Fragment(R.layout.ffr_fcr_record_list_fragment) {

    private var binding: FfrFcrRecordListFragmentBinding by autoCleared()

    private var adapter: FcrRecordListAdapter by autoCleared()

    private val viewModel: FcrRecordListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrFcrRecordListFragmentBinding.bind(view)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupList()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeRecords()
        }
    }

    private fun setupList() {
        adapter = FcrRecordListAdapter()
        binding.list.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 16))
        binding.list.adapter = adapter
    }

    private fun CoroutineScope.observeRecords() = launch {
        viewModel.recordsUiState
            .collect { uiState ->
                if (uiState is LoadingState.Success) {
                    adapter.submitList(uiState.data)
                }
                binding.loadingStateView.bind(uiState)
            }
    }

}