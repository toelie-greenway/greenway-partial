package greenway_myanmar.org.features.thread.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.ThreadFragmentBinding
import greenway_myanmar.org.features.thread.presentation.epoxy.controller.ThreadController
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThreadFragment : Fragment(R.layout.thread_fragment) {

    private val binding by viewBinding(ThreadFragmentBinding::bind)
    private val viewModel by viewModels<ThreadViewModel>()

    private val navController: NavController by lazy {
        findNavController()
    }
    private val controller: ThreadController = ThreadController(
        onGoToVoteClicked = {
            navigateToVotingScreen()
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
            observeVoteOptions()
        }
    }

    private fun setupList() {
        binding.list.setController(controller)
    }

    private fun CoroutineScope.observeVoteOptions() = launch {
        viewModel.uiState
            .collect {
                controller.setData(it)
            }
    }

    private fun navigateToVotingScreen() {
        val votedTags = viewModel.uiState.value.tagVoteOptions
        val category = viewModel.uiState.value.category ?: return
        val cropOrAnimalName = viewModel.uiState.value.cropOrAnimalName
        val categoryType = viewModel.uiState.value.categoryType

        navController.navigate(
            ThreadFragmentDirections.actionThreadFragmentToVotingFragment(
                votedTags.toTypedArray(),
                category,
                cropOrAnimalName,
                categoryType
            )
        )
    }
}