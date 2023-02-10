package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.epoxycontroller

import android.view.View.OnClickListener
import com.airbnb.epoxy.EpoxyController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.EpoxyLoadingStateBindingModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.FishPickerListItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.FishesUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.views.FishPickerListItemViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.vo.Resource
import greenway_myanmar.org.vo.Status

class FishPickerController(val clickCallback: ClickCallback) : EpoxyController() {

    private var _userFishesResource: Resource<List<UiFish>>? = null
    private var _fishesUiState: FishesUiState = LoadingState.Loading

    override fun buildModels() {

        if (_userFishesResource?.status == Status.LOADING) {
            EpoxyLoadingStateBindingModel_().id("loading").resource(_userFishesResource).addTo(this)
            return
        }

//
//    userCropsResource?.let { resource ->
//      resource.data?.let { items ->
//        if (!items.isNullOrEmpty()) {
//          CropPickerSubtitleBindingModel_()
//            .id("user_crop_subtitle")
//            .title("သင့်သီးနှံများ")
//            .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
//            .addTo(this)
//          items.forEach { item ->
//            CropPickerListItemBindingModel_()
//              .id("user_crop" + item.id)
//              .item(CheckableItem(item))
//              .clickCallback(clickCallback)
//              .addTo(this)
//          }
//
//          HorizontalDividerWithMarginBindingModel_()
//            .id("divider_1")
//            .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
//            .addTo(this)
//        }
//      }
//    }
//

        buildFishListUi()
//    cropListResource?.let { resource ->
//      resource.data?.let { items ->
//        CropPickerSubtitleBindingModel_()
//          .id("crop_list_subtitle")
//          .title("သီးနှံအားလုံး")
//          .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
//          .addTo(this)
//
//        items.forEach { item ->
//          CropPickerListItemBindingModel_()
//            .id("crop_" + item.id)
//            .item(CheckableItem(item))
//            .clickCallback(clickCallback)
//            .addTo(this)
//        }
//      }
//    }
    }

    private fun buildFishListUi() {
        when (_fishesUiState) {
            is LoadingState.Success -> {
                buildFishList()
            }
            is LoadingState.Error -> {

            }
            LoadingState.Loading -> {

            }
            else -> {

            }
        }
    }

    private fun buildFishList() {
        (_fishesUiState as LoadingState.Success).data.forEach { item ->
            FishPickerListItemViewModel_()
                .id("fish_" + item.fish.id)
                .item(FishPickerListItemUiState(item.fish, item.checked))
                .onItemClickCallback(OnClickListener { clickCallback.onItemClick(item.fish) })
                .addTo(this)
        }
    }

    fun setUserFishesResource(resource: Resource<List<UiFish>>) {
        _userFishesResource = resource
        requestModelBuild()
    }

    fun setFishesUiState(uiState: FishesUiState) {
        _fishesUiState = uiState
        requestModelBuild()
    }

    interface ClickCallback {
        fun onItemClick(selectedFish: UiFish)
    }
}
