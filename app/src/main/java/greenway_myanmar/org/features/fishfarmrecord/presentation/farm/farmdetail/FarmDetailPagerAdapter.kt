package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons.ClosedSeasonsFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.CropIncomeFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list.FcrRecordListFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonFragment

class FarmDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val pageCount = FarmDetailTabUiState.values().size

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FarmDetailTabUiState.OpeningSeason.index -> {
                OpeningSeasonFragment()
            }
            FarmDetailTabUiState.Fcr.index -> {
                FcrRecordListFragment()
            }
            FarmDetailTabUiState.CropIncome.index -> {
                CropIncomeFragment()
            }
            FarmDetailTabUiState.ClosedSeasons.index -> {
                ClosedSeasonsFragment()
            }
            else -> {
                throw IllegalArgumentException("Unsupported position: $position")
            }
        }
    }
}