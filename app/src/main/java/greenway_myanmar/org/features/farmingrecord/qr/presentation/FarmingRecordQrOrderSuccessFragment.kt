package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FarmingRecordQrOrderSuccessFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class FarmingRecordQrOrderSuccessFragment : Fragment() {

    private var binding by autoCleared<FarmingRecordQrOrderSuccessFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FarmingRecordQrOrderSuccessFragmentBinding.inflate(inflater, container, false).apply {
                viewOrderStatusButton.setOnClickListener {
                    navigateToOrderStatusScreen()
                }
            }
        return binding.root
    }

    private fun navigateToOrderStatusScreen() {
        findNavController().navigate(
            FarmingRecordQrOrderSuccessFragmentDirections
                .actionFarmingRecordQrOrderSuccessFragmentToFarmingRecordQrOrderStatusFragment()
        )
    }
}
