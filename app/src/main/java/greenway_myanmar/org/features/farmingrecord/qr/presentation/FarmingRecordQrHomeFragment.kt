package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FarmingRecordQrHomeFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class FarmingRecordQrHomeFragment : Fragment() {

  private var binding by autoCleared<FarmingRecordQrHomeFragmentBinding>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FarmingRecordQrHomeFragmentBinding.inflate(inflater, container, false).apply {
      newQrButton.setOnClickListener {
        navigateToAddEditQrScreen()
      }
    }
    return binding.root
  }

  private fun navigateToAddEditQrScreen() {
    findNavController().navigate(
      FarmingRecordQrHomeFragmentDirections.actionFarmingRecordQrHomeFragmentToAddEditFarmingRecordQrFragment()
    )
  }
}
