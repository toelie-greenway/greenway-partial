package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.core.presentation.util.numberFormat
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFarmDetailFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FarmDetailFragment : Fragment(R.layout.ffr_farm_detail_fragment) {

    private val viewModel: FarmDetailViewModel by viewModels()
    private val binding by viewBinding(FfrFarmDetailFragmentBinding::bind)
    private var adapter: FarmDetailPagerAdapter by autoCleared()
    private val onPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.addNewFcrButton.isVisible = position == FarmDetailTabUiState.Fcr.index
            binding.addNewCropIncomeButton.isVisible =
                position == FarmDetailTabUiState.CropIncome.index
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(
            view,
            getString(R.string.ffr_transition_name_screen_farm_detail)
        )
        setupUi()
        observeViewModel()
    }

    private fun setupTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // The drawing view is the id of the view above which the container transform
            // will play in z-space.
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
            // Set the color of the scrim to transparent as we also want to animate the
            // list fragment out of view
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }

    private fun setupUi() {
        setupToolbar()
        setupViewPager()
        setupTabLayout()
        setupAddNewFcrButton()
        setupAddNewCropIncomeButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupViewPager() {
        adapter = FarmDetailPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(FarmDetailTabUiState.values()[position].textResId)
        }.attach()
    }

    private fun setupAddNewFcrButton() {
        binding.addNewFcrButton.setOnClickListener {
            navigateToAddEditFcrRecordScreen()
        }
    }

    private fun setupAddNewCropIncomeButton() {
        binding.addNewCropIncomeButton.setOnClickListener {
            navigateToAddEditCropIncomeScreen()
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeFarm()
        }
    }

    private fun CoroutineScope.observeFarm() = launch {
        viewModel.farmUiState.collect { uiState ->
            Timber.d("UiState: $uiState")
            when (uiState) {
                is LoadingState.Success -> {
                    bindFarmUi(uiState.data)
                }
                else -> {
                    /* no-op */
                }
            }
        }
    }

    private fun bindFarmUi(farm: UiFarm) {
        binding.farmNameTextView.text = farm.name
        binding.farmSeasonInfoTextView.text = buildFarmSeasonInfo(farm)
        binding.seasonInfoCard.isVisible = farm.ongoingSeason != null

        if (farm.images.isNotEmpty()) {
            binding.farmImageView.load(
                requireContext(),
                farm.images.first()
            )
        }
        if (farm.ongoingSeason != null) {
            bindSeasonUi(farm.ongoingSeason)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindSeasonUi(season: Season) {
        binding.totalExpenseTextView.setAmount(season.totalExpenses)
        if (season.fishes.isNotEmpty()) {
            binding.fishIconImageView.load(requireContext(), season.fishes.first().iconImageUrl)
        }

        val fishCount = season.fishes.size
        if (fishCount > 1) {
            binding.moreFishGroup.isVisible = true
            binding.moreFishCountTextView.text = "+${numberFormat.format(fishCount - 1)}"
            binding.fishIconMoreImageView.load(requireContext(), season.fishes[1].iconImageUrl)
        } else {
            binding.moreFishGroup.isVisible = false
        }
    }

    private fun buildFarmSeasonInfo(
        farm: UiFarm
    ): String {
        val farmSeasonInfo = StringBuilder()
        farmSeasonInfo.append(
            resources.getString(
                R.string.ffr_formatted_farm_area, numberFormat.format(
                    farm.area.value
                )
            )
        )
        val season = farm.ongoingSeason
        if (season != null) {
            farmSeasonInfo.append(" · ")

            if (season.fishes.isNotEmpty()) {
                val firstFish = season.fishes.first()
                farmSeasonInfo.append(firstFish.name)
                if (firstFish.species.isNotEmpty()) {
                    farmSeasonInfo.append("(")
                    farmSeasonInfo.append(firstFish.species)
                    farmSeasonInfo.append(")")
                }
            }

            val fishCount = season.fishes.size
            if (fishCount > 1) {
                farmSeasonInfo.append("+")
                farmSeasonInfo.append(numberFormat.format(fishCount - 1))
            }

            if (season.name.isNotEmpty()) {
                farmSeasonInfo.append(" · ")
                farmSeasonInfo.append(season.name)
            }
        }
        return farmSeasonInfo.toString()
    }

    override fun onDestroyView() {
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        super.onDestroyView()
    }

    private fun navigateToAddEditFcrRecordScreen() {
        val season = viewModel.currentUiState.openingSeason ?: return
        val fishes = season.fishes
        if (fishes.isNullOrEmpty()) {
            return
        }

        navController.navigate(
            FarmDetailFragmentDirections.actionFarmDetailFragmentToAddEditFcrRecordFragment(
                fishes = fishes.toTypedArray(),
                seasonId = season.id
            )
        )
    }

    private fun navigateToAddEditCropIncomeScreen() {
        val seasonId = viewModel.currentUiState.openingSeason?.id ?: return
        if (seasonId.isEmpty()) {
            return
        }

        navController.navigate(
            FarmDetailFragmentDirections.actionFarmDetailFragmentToAddEditCropIncomeFragment(
                seasonId = seasonId
            )
        )
    }

}