/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package greenway_myanmar.org.vo;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import greenway_myanmar.org.ui.common.LoadNextPageCallback;
import greenway_myanmar.org.ui.common.RefreshCallback;
import greenway_myanmar.org.ui.common.RetryCallback;

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
public class Listing<T> {
    // the LiveData of paged lists for the UI to observe
    private final LiveData<PagedList<T>> pagedList;
    // represents the network request status to show to the user
    private final LiveData<NetworkState> networkState;
    // represents the refresh status to show to the user. Separate from networkState, this
    // value is importantly only when refresh is requested.
    private final LiveData<NetworkState> refreshState;
    // refreshes the whole data and fetches it from scratch.
    private final LiveData<Boolean> hasMore;
    private final RefreshCallback refreshCallback;
    // retries any failed requests.
    private final RetryCallback retryCallback;
    // load next page.
    private final LoadNextPageCallback loadNextPageCallback;
    // the LiveData of total item count
    private LiveData<Integer> itemCount;

//    public Listing(
//            LiveData<PagedList<T>> pagedList,
//            LiveData<NetworkState> networkState,
//            LiveData<NetworkState> refreshState,
//            RefreshCallback refreshCallback,
//            RetryCallback retryCallback) {
//        this.pagedList = pagedList;
//        this.networkState = networkState;
//        this.refreshState = refreshState;
//        this.hasMore = new MutableLiveData<Boolean>(false);
//        this.refreshCallback = refreshCallback;
//        this.retryCallback = retryCallback;
//        this.loadNextPageCallback = () -> { /* no-op */};
//    }

    public Listing(
            LiveData<PagedList<T>> pagedList,
            LiveData<NetworkState> networkState,
            LiveData<NetworkState> refreshState,
            LiveData<Boolean> hasMore,
            RefreshCallback refreshCallback,
            RetryCallback retryCallback,
            LoadNextPageCallback loadNextPageCallback
    ) {
        this.pagedList = pagedList;
        this.networkState = networkState;
        this.refreshState = refreshState;
        this.hasMore = hasMore;
        this.refreshCallback = refreshCallback;
        this.retryCallback = retryCallback;
        this.loadNextPageCallback = loadNextPageCallback;
    }

    public Listing(
            LiveData<PagedList<T>> pagedList,
            LiveData<Integer> itemCount,
            LiveData<NetworkState> networkState,
            LiveData<NetworkState> refreshState,
            LiveData<Boolean> hasMore,
            RefreshCallback refreshCallback,
            RetryCallback retryCallback,
            LoadNextPageCallback loadNextPageCallback
    ) {
        this.pagedList = pagedList;
        this.itemCount = itemCount;
        this.networkState = networkState;
        this.refreshState = refreshState;
        this.hasMore = hasMore;
        this.refreshCallback = refreshCallback;
        this.retryCallback = retryCallback;
        this.loadNextPageCallback = loadNextPageCallback;
    }

    public LiveData<PagedList<T>> getPagedList() {
        return pagedList;
    }

    public LiveData<Integer> getItemCount() {
        return itemCount;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<NetworkState> getRefreshState() {
        return refreshState;
    }

    public LiveData<Boolean> getHasMore() {
        return hasMore;
    }

    public RefreshCallback getRefreshCallback() {
        return refreshCallback;
    }

    public RetryCallback getRetryCallback() {
        return retryCallback;
    }

    public LoadNextPageCallback getLoadNextPageCallback() {
        return loadNextPageCallback;
    }
}