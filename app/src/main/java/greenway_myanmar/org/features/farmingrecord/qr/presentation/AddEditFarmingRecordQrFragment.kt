package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class AddEditFarmingRecordQrFragment : Fragment() {

  private var binding by autoCleared<AddEditFarmingRecordQrFragmentBinding>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding =
      AddEditFarmingRecordQrFragmentBinding.inflate(inflater, container, false).apply {
        submitButton.setOnClickListener { navigateToSummaryScreen() }
      }
    return binding.root
  }

  private fun navigateToSummaryScreen() {
    findNavController()
      .navigate(R.id.action_addEditFarmingRecordQrFragment_to_addEditFarmingRecordQrSummaryFragment)
  }
}
