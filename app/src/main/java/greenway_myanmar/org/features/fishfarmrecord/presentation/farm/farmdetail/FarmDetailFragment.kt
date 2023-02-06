package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

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
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFarmDetailFragmentBinding
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.util.kotlin.viewBinding

@AndroidEntryPoint
class FarmDetailFragment : Fragment(R.layout.ffr_farm_detail_fragment) {

    private val viewModel: FarmDetailViewModel by viewModels()
    private val binding by viewBinding(FfrFarmDetailFragmentBinding::bind)
    private var adapter: FarmDetailPagerAdapter by autoCleared()
    private val onPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.addNewFcrButton.isVisible = position == FarmDetailTabUiState.Fcr.index
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

    private fun navigateToAddEditFcrRecordScreen() {
        navController.navigate(
            FarmDetailFragmentDirections.actionFarmDetailFragmentToAddEditFcrRecordFragment()
        )
    }

    private fun observeViewModel() {

    }

    override fun onDestroyView() {
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        super.onDestroyView()
    }

}