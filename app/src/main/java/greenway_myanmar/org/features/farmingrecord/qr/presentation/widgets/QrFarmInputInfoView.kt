package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FarmingRecordQrFarmInputInfoViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.QrFarmActivityAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils
import greenway_myanmar.org.ui.transition.Rotate
import java.time.Instant

class QrFarmInputInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = FarmingRecordQrFarmInputInfoViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private lateinit var adapter: QrFarmActivityAdapter
    private var toggle: Rotate
    private var _expanded = false

    init {
        orientation = VERTICAL
        setupList()

        toggle = Rotate()
        toggle.addTarget(binding.expandIcon)

        binding.showDetailContainer.setOnClickListener {
            _expanded = !_expanded
            toggleDropdownArrow()
            updateListVisibility()
            updateText()
        }

        updateListVisibility()
        updateText()
    }

    private fun updateListVisibility() {
        binding.farmInputList.isVisible = _expanded
    }

    private fun updateText() {
        binding.showDetailTextView.setText(
            if (_expanded) {
                R.string.button_text_farming_record_qr_toggle_hide_farm_input
            } else {
                R.string.button_text_farming_record_qr_toggle_show_farm_input
            }
        )
    }

    private fun setupList() {
        adapter = QrFarmActivityAdapter()
        ViewCompat.setNestedScrollingEnabled(binding.farmInputList, false)
        binding.farmInputList.adapter = adapter
    }

    fun setData(season: UiSeason, activities: List<UiFarmActivity>) {
        setSeason(season)
        setActivities(activities)
    }

    private fun setSeason(season: UiSeason) {
        val latestHarvestedYear =
            DateUtils.format(season.latestHarvestedDate ?: Instant.now(), "yyyy")
        binding.seasonInfo.text =
            resources.getString(
                R.string.label_farming_record_qr_season_info,
                season.name,
                latestHarvestedYear
            )
    }

    private fun setActivities(activities: List<UiFarmActivity>) {
        adapter.submitList(activities)
    }

    private fun toggleDropdownArrow(resetAnimationDuration: Long = 200L) {
        toggle.duration = if (_expanded) 300L else resetAnimationDuration
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, toggle)
        binding.expandIcon.rotation = if (_expanded) 180f else 0f
    }

}
