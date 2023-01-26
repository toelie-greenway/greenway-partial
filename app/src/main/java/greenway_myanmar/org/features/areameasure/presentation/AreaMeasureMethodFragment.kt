package greenway_myanmar.org.features.areameasure.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AreaMeasureMethodFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(requireContext())
            .apply {
                text = "Area Measure Method"
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launchAndRepeatWithViewLifecycle {
            launch {
                delay(700)
                setResult(AreaMeasureMethod.Pin)
            }
        }
    }

    private fun setResult(method: AreaMeasureMethod) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RESULT_KEY_MEASURE_METHOD,
            method
        )
        dismiss()
    }

    companion object {
        const val REQUEST_KEY = "request_Keys.AREA_MEASURE"
        const val RESULT_KEY_MEASURE_METHOD = "result_keys.MEASURE_METHOD"
    }

}