package greenway_myanmar.org.features.areameasure.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AreaMeasureMapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(requireContext())
            .apply {
                text = "Area Measure"
            }
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    launchAndRepeatWithViewLifecycle {
      launch {
        delay(700)
        setResult()
      }
    }
  }

    private fun setResult() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RESULT_KEY_MEASURE_MAP,
//            AreaMeasurement.Area(
//                coordinates = listOf(
//                    LatLng(2.1, 2.2),
//                    LatLng(2.5, 2.5),
//                    LatLng(2.7, 2.7),
//                    LatLng(2.1, 2.1),
//                ),
//                acre = 5.69,
//                measurementType = AreaMeasureMethod.Walk
//            ),
            AreaMeasurement.Location(
                latLng = LatLng(2.0, 9.8912),
                measurementType = AreaMeasureMethod.Pin
            ),
        )
        findNavController().popBackStack()
    }

    companion object {
        const val ARG_MEASURE_METHOD = "args.MEASURE_METHOD"
        const val REQUEST_KEY = "request_Keys.AREA_MEASURE_MAP"
        const val RESULT_KEY_MEASURE_MAP = "result_keys.MEASURE_MAP"

        fun newInstance(measureMethod: AreaMeasureMethod) = AreaMeasureMapFragment().apply {
            arguments = bundleOf(
                ARG_MEASURE_METHOD to measureMethod,
            )
        }
    }
}
