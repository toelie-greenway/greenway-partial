package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.ponddetail

import android.os.Bundle
import android.view.View
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

@AndroidEntryPoint
class PondDetailFragment : Fragment(R.layout.ffrb_pond_detail_fragment) {

    private val viewModel: PondDetailViewModel by viewModels()
    private var binding: FfrbPondDetailFragmentBinding by autoCleared()
    private var adapter: PondDetailPagerAdapter by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

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
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }

    private fun observeViewModel() {

    }

}