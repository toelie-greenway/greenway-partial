package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import greenway_myanmar.org.databinding.FarmingRecordQrFarmingInfoViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.QrFarmActivityItemUiState

class QrFarmingInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = FarmingRecordQrFarmingInfoViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        orientation = VERTICAL
    }

    fun setActivities(activities: List<QrFarmActivityItemUiState>) {
        binding.farmInputInfoView.setActivities(activities)
    }


}
