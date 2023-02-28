package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.core.presentation.model.getDataOrNull
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FfrSeasonSummaryFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.controller.SeasonSummaryController
import greenway_myanmar.org.util.ShareLayoutUtil
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class SeasonSummaryFragment : Fragment(greenway_myanmar.org.R.layout.ffr_season_summary_fragment) {

    private val binding by viewBinding(FfrSeasonSummaryFragmentBinding::bind)
    private val viewModel by viewModels<SeasonSummaryViewModel>()
    private lateinit var controller: SeasonSummaryController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupToolbar()
        setupList()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeSeasonSummary()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.inflateMenu(greenway_myanmar.org.R.menu.ffr_season_summary)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == greenway_myanmar.org.R.id.action_share) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    shareSummaryAsImage()
                }
                true
            } else {
                false
            }
        }
    }

    private fun shareSummaryAsImage() {
        ShareLayoutUtil.share(requireContext(), binding.epoxyRecyclerView)
    }

    private fun setupList() {
        controller = SeasonSummaryController(requireContext())
        binding.epoxyRecyclerView.apply { setController(controller) }
    }

    private fun CoroutineScope.observeSeasonSummary() = launch {
        viewModel.seasonSummary.collect { state ->
            Timber.d("LoadingState: $state")
            binding.loadingStateView.bind(state)
            controller.setSeasonSummary(state.getDataOrNull())
        }
    }
}