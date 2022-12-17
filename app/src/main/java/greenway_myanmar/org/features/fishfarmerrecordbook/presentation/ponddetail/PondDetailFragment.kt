package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.ponddetail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbHomeFragmentBinding
import greenway_myanmar.org.databinding.FfrbPondDetailFragmentBinding
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.home.FishFarmerRecordBookHomeViewModel
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlin.properties.Delegates

@AndroidEntryPoint
class PondDetailFragment : Fragment(R.layout.ffrb_pond_detail_fragment) {

    private val viewModel: PondDetailViewModel by viewModels()
    private var binding: FfrbPondDetailFragmentBinding by autoCleared()
    private var adapter: PondDetailPagerAdapter by autoCleared()

    private val statusBarColor by lazy {
        requireActivity().window.statusBarColor
    }

    private val navController: NavController by lazy {
        findNavController()
    }
//
//    override fun onResume() {
//        super.onResume()
//        requireActivity().window.statusBarColor = Color.parseColor("#1F000000")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        requireActivity().window.statusBarColor =
//            ContextCompat.getColor(requireContext(), R.color.color_primary_variant)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbPondDetailFragmentBinding.bind(view)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        adapter = PondDetailPagerAdapter(this)
        binding.viewPager.adapter = adapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.ffr_tab_opening_season)
                }
                1 -> {
                    tab.text = getString(R.string.ffr_tab_closed_seasons)
                }
            }
        }.attach()
    }

    private fun observeViewModel() {

    }

}