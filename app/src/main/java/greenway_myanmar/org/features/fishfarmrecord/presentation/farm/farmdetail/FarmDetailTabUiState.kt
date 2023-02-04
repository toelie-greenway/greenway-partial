package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

import androidx.annotation.StringRes
import greenway_myanmar.org.R

enum class FarmDetailTabUiState(val index: Int, @StringRes val textResId: Int) {
    OpeningSeason(0, R.string.ffr_tab_opening_season),
    Fcr(1, R.string.ffr_tab_fcr),
    CropIncome(2, R.string.ffr_tab_crop_income),
    ClosedSeasons(3, R.string.ffr_tab_closed_seasons),
}