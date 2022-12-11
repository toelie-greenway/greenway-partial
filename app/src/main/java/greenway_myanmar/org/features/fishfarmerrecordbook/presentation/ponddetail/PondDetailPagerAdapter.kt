package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.ponddetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.finishedseasons.FinishedSeasonsFragment
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.ongoingseason.OngoingSeasonFragment

class PondDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                OngoingSeasonFragment()
            }
            1 -> {
                FinishedSeasonsFragment()
            }
            else -> {
                throw IllegalArgumentException("Unsupported position: $position")
            }

        }
    }
}