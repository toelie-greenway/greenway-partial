package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.ponddetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.closedseasons.ClosedSeasonsFragment
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.OpeningSeasonFragment

class PondDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                OpeningSeasonFragment()
            }
            1 -> {
                ClosedSeasonsFragment()
            }
            else -> {
                throw IllegalArgumentException("Unsupported position: $position")
            }

        }
    }
}