package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFade
import com.greenwaymyanmar.core.presentation.model.getDataOrNull
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrHomeFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FishFarmerRecordBookHomeFragment : Fragment(R.layout.ffr_home_fragment) {

    private val viewModel: FishFarmerRecordBookHomeViewModel by viewModels()
    private var binding: FfrHomeFragmentBinding by autoCleared()
    private var adapter: FarmListAdapter by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrHomeFragmentBinding.bind(view)
        ViewCompat.setTransitionName(view, getString(R.string.ffr_transition_name_screen_farm_list))
        ViewGroupCompat.setTransitionGroup(view as ViewGroup, true)
        postponeTransition()
        setupFragmentResultListener()
        setupUi()
        observeViewModel()
    }

    private fun postponeTransition() {
        postponeEnterTransition()
        requireView().doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(REQUEST_KEY_ADD_NEW_POND) { _, bundle ->
            val newPondCreated = bundle.getBoolean(BUNDLE_KEY_NEW_POND_CREATED)
            val pondId = bundle.getString(BUNDLE_KEY_POND_ID)
            if (newPondCreated && !pondId.isNullOrEmpty()) {
                openAddEditSeasonScreen(requireView(), pondId)
            }
        }
    }

    private fun setupUi() {
        setupSwipeRefresh()
        setupNewFarmFab()
        setupPondListUi()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.farms
                    .collect { state ->
                        adapter.submitList(state.getDataOrNull())
                        binding.loadingStateView.bind(state)
                    }
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setScrollUpChild(binding.pondList)
    }

    private fun setupNewFarmFab() {
        ViewCompat.setTransitionName(
            binding.addNewFarmFab,
            getString(R.string.ffr_transition_name_fab_add_farm)
        )
        binding.addNewFarmFab.apply {
            setOnClickListener(this) { openAddEditFishPondScreen() }
        }
    }

    private fun fadeVisible(view: View) {
        val materialFade = MaterialFade().apply {
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
        }
        materialFade.addTarget(view)
        TransitionManager.beginDelayedTransition(binding.coordinatorLayout, materialFade)
        view.visibility = View.VISIBLE
    }

    private fun setOnClickListener(view: View, onClick: () -> Unit) {
        view.setOnClickListener { onClick() }
    }

    private fun setupPondListUi() {
        adapter = FarmListAdapter(
            onItemClick = { view, item ->
                openFarmDetailScreen(view, item)
            },
            onCompanyClick = {
                openCompanyScreen(it)
            },
            onAddNewSeasonClick = { view, item ->
                openAddEditSeasonScreen(view, item.id)
            },
            onAddNewExpenseClick = { view, item ->
                openAddEditExpenseScreen(view, item)
            }
        )
        binding.pondList.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 16))
        binding.pondList.adapter = adapter
    }

    private fun openCompanyScreen(company: ContractFarmingCompany) {
        // Toast.makeText(requireContext(), "TODO:// Open company screen", Toast.LENGTH_SHORT).show()
    }

    private fun openAddEditFishPondScreen() {
        exitTransition = Hold().apply {
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
        }
        reenterTransition = Hold().apply {
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
        }
        val addEditFarmTransitionName =
            getString(R.string.ffr_transition_name_screen_add_edit_farm)
        val extras =
            FragmentNavigatorExtras(
                binding.addNewFarmFab to addEditFarmTransitionName
            )
        val directions =
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditPondFragment()
        navController.navigate(directions, extras)
    }

    private fun openAddEditSeasonScreen(view: View, farmId: String) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(
                R.integer.greenway_motion_duration_large
            ).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(
                R.integer.greenway_motion_duration_large
            ).toLong()
        }
        val addEditSeasonTransitionName = getString(
            R.string.ffr_transition_name_screen_add_edit_season
        )
        val extras = FragmentNavigatorExtras(
            view to addEditSeasonTransitionName
        )
        val directions =
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditSeasonFragment(
                farmId
            )
        navController.navigate(directions, extras)
    }

    private fun openAddEditExpenseScreen(view: View, item: FarmListItemUiState) {
        val farmId = item.id
        val seasonId = item.openingSeason?.id ?: return

        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(
                R.integer.greenway_motion_duration_large
            ).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(
                R.integer.greenway_motion_duration_large
            ).toLong()
        }
        val addEditExpenseTransitionName = getString(
            R.string.ffr_transition_name_screen_add_edit_expense
        )
        val extras = FragmentNavigatorExtras(
            view to addEditExpenseTransitionName
        )
        val directions =
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditExpenseFragment(
                farmId = farmId,
                seasonId = seasonId
            )
        navController.navigate(directions, extras)
    }

    private fun openFarmDetailScreen(view: View, item: FarmListItemUiState) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(
                R.integer.greenway_motion_duration_large
            ).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(
                R.integer.greenway_motion_duration_large
            ).toLong()
        }
        val farmDetailTransitionName = getString(
            R.string.ffr_transition_name_screen_farm_detail
        )
        val extras = FragmentNavigatorExtras(
            view to farmDetailTransitionName
        )
        val directions = FishFarmerRecordBookHomeFragmentDirections
            .actionHomeFragmentToFarmDetailFragment(
                farmId = item.id
            )
        navController.navigate(directions, extras)
    }

    companion object {
        const val REQUEST_KEY_ADD_NEW_POND = "request_key.ADD_NEW_POND"
        const val BUNDLE_KEY_NEW_POND_CREATED = "bundle_key.NEW_POND_CREATED"
        const val BUNDLE_KEY_POND_ID = "bundle_key.POND_ID"
    }


}