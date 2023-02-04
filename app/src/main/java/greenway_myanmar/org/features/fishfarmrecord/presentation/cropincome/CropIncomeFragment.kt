package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrCropIncomeFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class CropIncomeFragment : Fragment(R.layout.ffr_crop_income_fragment) {

    private var binding: FfrCropIncomeFragmentBinding by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrCropIncomeFragmentBinding.bind(view)
    }
}