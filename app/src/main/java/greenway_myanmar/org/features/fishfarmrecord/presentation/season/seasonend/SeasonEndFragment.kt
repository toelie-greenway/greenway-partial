package greenway_myanmar.org.features.fishfarmrecord.presentation.season.seasonend

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.core.presentation.model.isLoading
import com.greenwaymyanmar.core.presentation.model.isNotLoading
import com.greenwaymyanmar.core.presentation.model.isSuccess
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.FfrSeasonEndFragmentBinding
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeasonEndFragment : Fragment(R.layout.ffr_season_end_fragment) {

    private val binding by viewBinding(FfrSeasonEndFragmentBinding::bind)
    private val viewModel by viewModels<SeasonEndViewModel>()
    private val navController by lazy {
        findNavController()
    }
    private lateinit var adapter: SeasonEndReasonListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupToolbar()
        setupList()
        setupSubmitButton()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeSeasonEndReasons()
            observeReasonError()
            observeSavingEndSeason()
        }
    }

    private fun setupList() {
        adapter = SeasonEndReasonListAdapter(onItemClick = {
            onReasonItemClicked(it)
        })
        binding.list.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.list.adapter = adapter
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(SeasonEndEvent.OnSubmit)
        }
    }

    private fun onReasonItemClicked(item: SeasonEndReasonListItemUiState) {
        viewModel.handleEvent(SeasonEndEvent.OnReasonChanged(item))
    }

    private fun CoroutineScope.observeSeasonEndReasons() = launch {
        viewModel.reasonsUiState.collect { uiState ->
            if (uiState is LoadingState.Success) {
                adapter.submitList(uiState.data)
            }
            binding.loadingStateView.bind(uiState)
        }
    }

    private fun CoroutineScope.observeReasonError() = launch {
        viewModel.uiState.map { it.reasonError }
            .distinctUntilChanged()
            .collect { error ->
                if (error != null) {
                    showErrorMessage(error)
                }
            }
    }

    private fun showErrorMessage(error: Text) {
        showSnackbar(error)
        viewModel.handleEvent(SeasonEndEvent.OnReasonErrorShown)
    }

    private fun CoroutineScope.observeSavingEndSeason() = launch {
        viewModel.saveEndSeasonUiState.collect { uiState ->
            binding.endingSeasonContainer.isVisible = uiState.isLoading()
            binding.submitButton.isVisible = uiState.isNotLoading()
            if (uiState.isSuccess()) {
                navController.popBackStack()
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }


}