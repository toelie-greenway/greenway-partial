package greenway_myanmar.org.ui.croppicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCrop
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CropPickerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val textView = TextView(requireContext())
        textView.setText("Crop Picker")
        return textView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("Crop Picker!")
        launchAndRepeatWithViewLifecycle {
            launch {
                delay(400)
                setResult()
            }
        }
    }

    private fun setResult() {
        setFragmentResult(
            REQUEST_KEY_CROP, bundleOf(
                KEY_CROP to UiCrop(
                    id = "1",
                    name = "Test Crop",
                    iconImageUrl = ""
                )
            )
        )
        findNavController().popBackStack()
    }

    companion object {

        const val REQUEST_KEY_CROP = "request_key.CROP"
        const val KEY_CROP = "key.CROP"

        private const val AUTO_SEARCH_DELAY_MILLIS = 700L

        @JvmStatic
        fun newInstance() = CropPickerFragment().apply { arguments = Bundle().apply {} }
    }
}
