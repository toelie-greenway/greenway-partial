package com.greenwaymyanmar.common.feature.tag.presentation.voting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.controller.VotingController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.TagVotingFragmentBinding
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
        }
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
            observeVotedTags()
        }
    }

    private fun setupList() {
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.list.addItemDecoration(EpoxyItemSpacingDecorator(8.dp(requireContext()).toInt()))
        binding.list.setController(controller)
    }

    private fun CoroutineScope.observeVotedTags() = launch {
        viewModel.uiState
            .collect {
                controller.setData(it)
            }
    }

    private fun navigateToVotingScreen() {

    }

    private fun onVoteClicked(tag: UiVotableTag) {
        viewModel.handleEvent(
            VotingEvent.OnToggleVote(tag)
        )
    }

}