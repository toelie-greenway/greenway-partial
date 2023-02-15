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
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.FfrFcrRecordListFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmDetailViewModel
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FcrRecordListFragment : Fragment(R.layout.ffr_fcr_record_list_fragment) {

    private val binding by viewBinding(FfrFcrRecordListFragmentBinding::bind)

    private var adapter: FcrRecordListAdapter by autoCleared()

    private val viewModel: FcrRecordListViewModel by viewModels()
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
            observeRecords()
            observeFarmUiStateFromParentViewModel()
        }
    }

    private fun setupList() {
        adapter = FcrRecordListAdapter(
            onItemClicked = { onItemClicked(it) },
            onEditClicked = { onEdit(it) },
            onDeleteClicked = { onDelete(it) }
        )
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

    private fun CoroutineScope.observeFarmUiStateFromParentViewModel() = launch {
        parentViewModel.farmUiState
            .collect { farmUiState ->
                viewModel.handleEvent(FcrRecordListEvent.OnFarmChanged(farmUiState))
            }
    }

    private fun onItemClicked(item: FcrRecordListItemUiState) {
        
    }

    private fun onDelete(item: FcrRecordListItemUiState) {
        showComingSoonMessage()
    }

    private fun onEdit(item: FcrRecordListItemUiState) {
        showComingSoonMessage()
    }

    private fun showComingSoonMessage() {
        showSnackbar(Text.ResourceText(R.string.ffr_message_coming_soon))
    }

}