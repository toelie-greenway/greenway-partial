package greenway_myanmar.org.features.thread.presentation

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategoryType
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagProductsListingUseCase
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    getTagProductsListingUseCase: GetTagProductsListingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ThreadUiState>(ThreadUiState())
    val uiState = _uiState.asStateFlow()

    private val productsResult = MutableLiveData<Listing<Product>>()
    val products =
        Transformations.switchMap(productsResult) { it.pagedList }
    val productNetworkState: LiveData<NetworkState> =
        Transformations.switchMap(productsResult) { it.networkState }
    val hasMoreProduct: LiveData<Boolean> = Transformations.switchMap(productsResult) { it.hasMore }

    init {
        _uiState.update {
            it.copy(
                tagVoteOptions = listOf(
                    UiVotableTag(
                        id = "6",
                        tag = UiTag(
                            id = "6",
                            name = "ပင်စည်ပုပ်ရောဂါ",
                            category = UiCategory(
                                id = "1",
                                name = "ပိုးမွှားရောဂါ",
                                imageUrl = "https://picsum.photos/id/12/960/540",
                                type = UiCategoryType.Agri
                            ),
                            categories = null,
                            imageUrls = listOf(
                                "https://picsum.photos/id/210/960/540"
                            ),
                            color = Color.GREEN
                        ),
                        voteCount = 2,
                        isVoted = true,
                        suggestedCategory = null
                    )
                ),
                isTechnician = true,
            )
        }
        productsResult.value = getTagProductsListingUseCase()
    }

    fun loadProductNextPage() {
        productsResult.value?.loadNextPageCallback?.load()
    }
}