package com.greenwaymyanmar.common.feature.tag.presentation.voting

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.greenwaymyanmar.common.feature.category.presentation.categorypicker.CategoryPickerBottomSheetFragment
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.controller.VotingController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.TagVotingFragmentBinding
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class VotingFragment : Fragment(R.layout.tag_voting_fragment) {
    private val binding by viewBinding(TagVotingFragmentBinding::bind)
    private val viewModel by viewModels<VotingViewModel>()

    private val navController: NavController by lazy {
        findNavController()
    }

    private val controller: VotingController = VotingController(
        onVoteClicked = {
            onVoteClicked(it)
        },
        onChangeCategoryClicked = {
            onChangedCategoryClicked()
        },
        onClearCategoryClicked = {
            onClearCategoryClicked()
        }
    )

    private val args by navArgs<VotingFragmentArgs>()

    private lateinit var textChangeCountDownJob: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
        setupFragmentResultListener()
    }

    private fun setupUi() {
        setupToolbar()
        setupList()
        setupSubmitButton()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeUiState()
            observeSubmitButtonLabel()
            observeSubmittingState()
        }
        observeTagListing()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(CategoryPickerBottomSheetFragment.REQUEST_KEY_CATEGORY) { _, bundle ->
            bundle.getParcelableExtraCompat<UiCategory?>(CategoryPickerBottomSheetFragment.KEY_CATEGORY)
                ?.let { category ->
                    viewModel.handleEvent(
                        VotingEvent.OnCustomCategoryChanged(category)
                    )
                }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.inflateMenu(R.menu.tag_voting)
        val searchView = binding.toolbar.menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = getString(R.string.tag_voting_hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                /* no-op */
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d("newText: $newText")
                if (::textChangeCountDownJob.isInitialized)
                    textChangeCountDownJob.cancel()

                textChangeCountDownJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(800)
                    viewModel.handleEvent(VotingEvent.OnQueryChanged(newText.orEmpty()))
                }
                return true
            }
        })
    }

    private fun setupList() {
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.list.addItemDecoration(EpoxyItemSpacingDecorator(8.dp(requireContext()).toInt()))
        binding.list.setController(controller)
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(VotingEvent.OnSubmit)
        }
    }

    private fun CoroutineScope.observeUiState() = launch {
        viewModel.uiState
            .collect {
                controller.uiState = it

                it.category?.let {
                    binding.categoryChip.text = it.name
                }

                binding.cropOrAnimalNameTextView.text = it.cropOrAnimalName.orEmpty()
            }
    }

    private fun CoroutineScope.observeSubmitButtonLabel() = launch {
        viewModel.uiState.map { it.submitButtonLabel }
            .collect {
                binding.submitButton.setText(it)
            }
    }

    private fun CoroutineScope.observeSubmittingState() = launch {
        viewModel.uiState.map { it.submittingLoadingState }
            .distinctUntilChanged()
            .collect { state ->
                binding.submitButton.isVisible = state !is LoadingState.Loading
                binding.submittingStateContainer.isVisible = state is LoadingState.Loading
                when (state) {
                    is LoadingState.Error -> {
                        state.message?.let {
                            showSnackbar(it)
                        }
                    }
                    is LoadingState.Success -> {
                        navController.popBackStack()
                    }
                    else -> {
                        /* no-op */
                    }
                }
            }
    }

    private fun observeTagListing() {
        viewModel.tags.observe(viewLifecycleOwner) {
            controller.submitList(it)
        }
        viewModel.networkState.observe(viewLifecycleOwner) {
            Timber.d("NetworkState: ${it.status}")
            controller.setNetworkState(it)
        }
        viewModel.hasMore.observe(viewLifecycleOwner) {
            Timber.d("HasMore: $it")
            controller.setHasMore(it)
        }
    }

    private fun onVoteClicked(tag: UiVotableTag) {
        viewModel.handleEvent(
            VotingEvent.OnVoteChanged(tag)
        )
    }

    private fun onChangedCategoryClicked() {
        val categoryType = args.categoryType
        navController.navigate(
            VotingFragmentDirections.actionVotingFragmentToCategoryPickerBottomSheetFragment(
                categoryType
            )
        )
    }

    private fun onClearCategoryClicked() {
        viewModel.handleEvent(
            VotingEvent.OnCustomCategoryChanged(null)
        )
    }
}