package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrbHomeFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FishFarmerRecordBookHomeFragment : Fragment(R.layout.ffrb_home_fragment) {

    private val viewModel: FishFarmerRecordBookHomeViewModel by viewModels()
    private var binding: FfrbHomeFragmentBinding by autoCleared()
    private var adapter: FarmListAdapter by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbHomeFragmentBinding.bind(view)
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
        setupNewPondFab()
        setupPondListUi()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.ponds }
                    .distinctUntilChanged()
                    .collect {
                        adapter.submitList(it)
                    }
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setScrollUpChild(binding.pondList)
    }

    private fun setupNewPondFab() {
        ViewCompat.setTransitionName(
            binding.addNewPondFab,
            getString(R.string.ffr_transition_name_fab_add_farm)
        )
        binding.addNewPondFab.apply {
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
                openPondDetailScreen(view, item)
            },
            onCompanyClick = {
                openCompanyScreen(it)
            },
            onAddNewSeasonClick = { view, item ->
                openAddEditSeasonScreen(view, item.id)
            },
            onAddNewExpenseClick = {
                openAddEditExpenseScreen(it)
            }
        )
        binding.pondList.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.pondList.adapter = adapter
    }

    private fun openCompanyScreen(company: ContractFarmingCompany) {
        Toast.makeText(requireContext(), "TODO:// Open company screen", Toast.LENGTH_SHORT).show()
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
                binding.addNewPondFab to addEditFarmTransitionName
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

    private fun openAddEditExpenseScreen(item: FarmListItemUiState) {

        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditExpenseFragment()
        )
    }

    private fun openPondDetailScreen(view: View, item: FarmListItemUiState) {
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
            .actionHomeFragmentToPondDetailFragment(
                pondId = item.id
            )
        navController.navigate(directions, extras)
    }

    companion object {
        const val REQUEST_KEY_ADD_NEW_POND = "request_key.ADD_NEW_POND"
        const val BUNDLE_KEY_NEW_POND_CREATED = "bundle_key.NEW_POND_CREATED"
        const val BUNDLE_KEY_POND_ID = "bundle_key.POND_ID"
    }


}