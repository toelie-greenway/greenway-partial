package greenway_myanmar.org.features.tag.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.tag.domain.usecases.GetTagPostsStreamUseCase
import greenway_myanmar.org.features.tag.domain.usecases.GetTagProductsStreamUseCase
import greenway_myanmar.org.features.tag.domain.usecases.GetTagStreamUseCase
import greenway_myanmar.org.features.tag.domain.usecases.GetTagThreadsStreamUseCase
import greenway_myanmar.org.features.tag.presentation.model.UiTag
import greenway_myanmar.org.features.tag.presentation.model.UiTagPost
import greenway_myanmar.org.features.tag.presentation.model.UiTagProduct
import greenway_myanmar.org.features.tag.presentation.model.UiTagThread
import greenway_myanmar.org.util.WhileViewSubscribed
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
    getTagThreadsStreamUseCase: GetTagThreadsStreamUseCase,
    getTagPostsStreamUseCase: GetTagPostsStreamUseCase,
    getTagProductsStreamUseCase: GetTagProductsStreamUseCase
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

    private val threads: Flow<LoadingState<List<UiTagThread>>> = loadDataSignal.flatMapLatest {
        threadsStream(getTagThreadsStreamUseCase)
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    private val posts: StateFlow<LoadingState<List<UiTagPost>>> = loadDataSignal.flatMapLatest {
        postsStream(getTagPostsStreamUseCase)
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    private val products: StateFlow<LoadingState<List<UiTagProduct>>> =
        loadDataSignal.flatMapLatest {
            productsStream(getTagProductsStreamUseCase)
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    val uiState: StateFlow<TagUiState> = combine(
        currentSelectedTab,
        tag,
        threads,
        posts,
        products
    ) { tab, tag, threads, posts, products ->
        TagUiState(
            tab = tab,
            tag = tag,
            threads = threads,
            posts = posts,
            products = products
        )
    }.stateIn(viewModelScope, WhileViewSubscribed, TagUiState.Empty)

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

private fun threadsStream(getTagThreadsStreamUseCase: GetTagThreadsStreamUseCase): Flow<LoadingState<List<UiTagThread>>> {
    return getTagThreadsStreamUseCase()
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
                        UiTagThread.fromDomain(it)
                    })
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

private fun productsStream(getTagProductsStreamUseCase: GetTagProductsStreamUseCase): Flow<LoadingState<List<UiTagProduct>>> {
    return getTagProductsStreamUseCase()
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
                        UiTagProduct.fromDomain(it)
                    })
                }
            }
        }
}