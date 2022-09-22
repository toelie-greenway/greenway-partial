package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import greenway_myanmar.org.databinding.FarmingRecordQrFarmingInfoViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason

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

    fun setData(activities: List<UiQrFarmActivity>, farm: UiFarm, season: UiSeason) {
        setFarmingInfo(season, activities)
        setFarm(farm)
        setSeason(season)
    }

    private fun setFarmingInfo(season: UiSeason, activities: List<UiQrFarmActivity>) {
        binding.farmInputInfoView.setData(season, activities)
    }

    private fun setFarm(farm: UiFarm) {
        binding.farmName.text = farm.name
        binding.farmLocation.text = "//TODO:"
    }

    private fun setSeason(season: UiSeason) {
        binding.cropName.text = season.crop.title
    }

}
