package greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.AddEditFarmingRecordQrConfirmFragment
import greenway_myanmar.org.features.farmingrecord.qr.presentation.AddEditFarmingRecordQrFormFragment

class AddEditQrPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return TOTAL_PAGE
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FORM_PAGE_INDEX -> {
                AddEditFarmingRecordQrFormFragment.newInstance()
            }
            CONFIRM_PAGE_INDEX -> {
                AddEditFarmingRecordQrConfirmFragment.newInstance()
            }
            else -> {
                throw IllegalArgumentException("Unsupported position $position")
            }
        }
    }

    companion object {
        const val TOTAL_PAGE = 2
        const val FORM_PAGE_INDEX = 0
        const val CONFIRM_PAGE_INDEX = 1
    }
}