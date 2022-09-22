package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isVisible
import greenway_myanmar.org.common.domain.entities.Resource
import greenway_myanmar.org.databinding.FarmingRecordQrDetailViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrDetail

class QrDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = FarmingRecordQrDetailViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun bind(resource: Resource<UiQrDetail>?) {
        if (resource == null) {
            return
        }

        // loading
        binding.qrDetailLoadingIndicator.isVisible = resource.isLoading()

        // content
        val qrDetail: UiQrDetail? = resource.data
        binding.qrDetailContentContainer.isVisible = qrDetail != null
        if (qrDetail != null) {
            binding.qrImageView.setQrData(qrDetail.qrUrl)
            binding.farmingInfoView.setData(
                qrDetail.farmActivities,
                qrDetail.farm,
                qrDetail.season
            )
            binding.userInfoView.setData(qrDetail.user)
        }
    }
}