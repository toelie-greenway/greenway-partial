package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import greenway_myanmar.org.databinding.FarmingRecordQrUserInfoViewBinding


class QrUserInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = FarmingRecordQrUserInfoViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        orientation = VERTICAL
    }
}
