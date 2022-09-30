package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import greenway_myanmar.org.databinding.FarmingRecordQrFarmingInfoViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason

class QrFarmingInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var farm: UiFarm? = null

    private val binding = FarmingRecordQrFarmingInfoViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        orientation = VERTICAL
        binding.farmLocationShowMapButton.setOnClickListener {
            val latitude = farm?.latitude
            val longitude = farm?.longitude
            if (latitude != null && longitude != null) {
                openMap(latitude, longitude)
            }
        }
    }

    private fun openMap(latitude: Double, longitude: Double) {
        Toast.makeText(context, "Lat: $latitude, Lng: $longitude", Toast.LENGTH_SHORT).show()
    }

    fun setData(activities: List<UiFarmActivity>, farm: UiFarm, season: UiSeason) {
        setFarmingInfo(season, activities)
        setFarm(farm)
        setSeason(season)
    }

    private fun setFarmingInfo(season: UiSeason, activities: List<UiFarmActivity>) {
        binding.farmInputInfoView.setData(season, activities)
    }

    private fun setFarm(farm: UiFarm) {
        binding.farmName.text = farm.name

        if (farm.hasLatLng()) {
            binding.farmLocation.isVisible = false
            binding.farmLocationMapFrame.isVisible = true
        } else {
            binding.farmLocation.isVisible = true
            binding.farmLocationMapFrame.isVisible = false
            binding.farmLocation.text = farm.location
        }
    }

    private fun setSeason(season: UiSeason) {
        binding.cropName.text = season.crop.title
    }

}
