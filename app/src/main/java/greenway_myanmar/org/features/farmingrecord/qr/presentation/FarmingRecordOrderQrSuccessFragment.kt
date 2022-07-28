package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FarmingRecordOrderQrSuccessFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class FarmingRecordOrderQrSuccessFragment: Fragment() {

  private var binding by autoCleared<FarmingRecordOrderQrSuccessFragmentBinding>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FarmingRecordOrderQrSuccessFragmentBinding.inflate(inflater, container, false).apply {

    }
    return binding.root
  }
}
