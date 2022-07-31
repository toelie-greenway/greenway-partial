package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FarmingRecordQrOrderStatusFragmentBinding
import greenway_myanmar.org.databinding.FarmingRecordQrOrderSuccessFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class FarmingRecordQrOrderStatusFragment : Fragment() {

    private var binding by autoCleared<FarmingRecordQrOrderStatusFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FarmingRecordQrOrderStatusFragmentBinding.inflate(inflater, container, false).apply {

            }
        return binding.root
    }
}
