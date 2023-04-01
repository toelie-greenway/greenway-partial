package com.greenwaymyanmar.common.feature.tag.presentation.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagPostsStreamUseCase
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagProductsListingUseCase
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagStreamUseCase
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagThreadsListingUseCase
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagPost
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.util.WhileViewSubscribed
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Product
import greenway_myanmar.org.vo.Thread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    getTagStreamUseCase: GetTagStreamUseCase,
    private val getTagThreadsListingUseCase: GetTagThreadsListingUseCase,
    getTagPostsStreamUseCase: GetTagPostsStreamUseCase,
    getTagProductsListingUseCase: GetTagProductsListingUseCase
) : ViewModel() {

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private val currentSelectedTab = MutableStateFlow<UiTagTab>(UiTagTab.Thread)

    private val tag: StateFlow<LoadingState<UiTag>> = loadDataSignal.flatMapLatest {
        tagStream(getTagStreamUseCase)
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    private val threadsResult = MutableLiveData<Listing<Thread>>()
    val threads =
        Transformations.switchMap(threadsResult) { it.pagedList }
    val threadNetworkState: LiveData<NetworkState> =
        Transformations.switchMap(threadsResult) { it.networkState }
    val hasMoreThread: LiveData<Boolean> = Transformations.switchMap(threadsResult) { it.hasMore }

    private val productsResult = MutableLiveData<Listing<Product>>()
    val products =
        Transformations.switchMap(productsResult) { it.pagedList }
    val productNetworkState: LiveData<NetworkState> =
        Transformations.switchMap(productsResult) { it.networkState }
    val hasMoreProduct: LiveData<Boolean> = Transformations.switchMap(productsResult) { it.hasMore }

    private val posts: StateFlow<LoadingState<List<UiTagPost>>> = loadDataSignal.flatMapLatest {
        postsStream(getTagPostsStreamUseCase)
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    val uiState: StateFlow<TagUiState> = combine(
        currentSelectedTab,
        tag,
        posts,
    ) { tab, tag, posts ->
        TagUiState(
            tab = tab,
            tag = tag,
            threads = LoadingState.Idle,
            posts = posts,
            products = LoadingState.Idle
        )
    }.stateIn(viewModelScope, WhileViewSubscribed, TagUiState.Empty)

    init {
        threadsResult.value = getTagThreadsListingUseCase()
        productsResult.value = getTagProductsListingUseCase()
    }

    fun handleEvent(event: TagEvent) {
        when (event) {
            is TagEvent.OnTabChanged -> {
                updateTab(event.tab)
            }
        }
    }

    private fun updateTab(tab: UiTagTab) {
        currentSelectedTab.value = tab
    }

    fun loadThreadNextPage() {
        threadsResult.value?.loadNextPageCallback?.load()
    }

    fun loadProductNextPage() {
        productsResult.value?.loadNextPageCallback?.load()
    }
}

private fun tagStream(getTagStreamUseCase: GetTagStreamUseCase): Flow<LoadingState<UiTag>> {
    return getTagStreamUseCase().map { result ->
        when (result) {
            is Result.Error -> {
                LoadingState.Error(result.exception)
            }
            Result.Loading -> {
                LoadingState.Loading
            }
            is Result.Success -> {
                LoadingState.Success(data = UiTag.fromDomainModel(result.data))
            }
        }
    }
}

private fun postsStream(getTagPostsStreamUseCase: GetTagPostsStreamUseCase): Flow<LoadingState<List<UiTagPost>>> {
    return getTagPostsStreamUseCase()
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> {
                    LoadingState.Error(result.exception)
                }
                Result.Loading -> {
                    LoadingState.Loading
                }
                is Result.Success -> {
                    LoadingState.Success(data = result.data.map {
                        UiTagPost.fromDomain(it)
                    })
                }
            }
        }
}