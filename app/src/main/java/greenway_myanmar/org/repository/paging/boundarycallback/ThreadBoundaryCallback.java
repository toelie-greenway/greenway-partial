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

package greenway_myanmar.org.repository.paging.boundarycallback;

import android.arch.paging.PagingRequestHelper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.greenwaymyanmar.common.data.api.v1.Pagination;
import com.greenwaymyanmar.common.data.api.v1.response.ThreadListResponse;
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice;

import greenway_myanmar.org.AppExecutors;
import greenway_myanmar.org.db.GreenWayDb;
import greenway_myanmar.org.db.helper.UserHelper;
import greenway_myanmar.org.ui.threads.ThreadsViewModel;
import greenway_myanmar.org.util.PaginationUtils;
import greenway_myanmar.org.vo.NetworkState;
import greenway_myanmar.org.vo.Thread;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 *
 * <p>The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
public class ThreadBoundaryCallback extends PagedList.BoundaryCallback<Thread> {

    private final AppExecutors appExecutors;
    private final GreenWayDb db;
    private final GreenWayWebservice webservice;
    private final int networkPageSize;
    private final DataResponseCallback dataResponseCallback;
    private final PagingRequestHelper helper;
    private final UserHelper userHelper;
    private final ThreadsViewModel.Query query;
    private final LiveData<NetworkState> networkState;

    public ThreadBoundaryCallback(
            AppExecutors appExecutors,
            GreenWayDb db,
            GreenWayWebservice webservice,
            int networkPageSize,
            DataResponseCallback dataResponseCallback,
            UserHelper userHelper,
            ThreadsViewModel.Query query) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.webservice = webservice;
        this.networkPageSize = networkPageSize;
        this.dataResponseCallback = dataResponseCallback;
        helper = new PagingRequestHelper(appExecutors.diskIO());
        this.userHelper = userHelper;
        this.query = query;
        networkState = helper.createStatusLiveData();
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @Override
    public void onZeroItemsLoaded() {
        helper.runIfNotRunning(
                PagingRequestHelper.RequestType.INITIAL,
                request -> {
                    webservice
                            .getThreads(
                                    1,
                                    networkPageSize,
                                    userHelper.getActiveUserId(),
                                    query.toApiQueryMap()
                            )
                            .enqueue(new WebserviceCallback(request));
                    //            // category_id
                    //            if (query.filterType == ThreadRepository.FILTER_TYPE_LIVESTOCK) {
                    //
                    // webservice.getThreadsByLivestockId(userHelper.getActiveUserId(),
                    // query.key).enqueue(new WebserviceCallback(request));
                    //            } else if (query.filterType == ThreadRepository.FILTER_TYPE_CROP)
                    // {
                    //                webservice.getThreadsByCropNames(userHelper.getActiveUserId(),
                    // query.key).enqueue(new WebserviceCallback(request));
                    //            } else {
                    //
                    // webservice.getThreads(userHelper.getActiveUserId()).enqueue(new
                    // WebserviceCallback(request));
                    //            }
                });
    }

    /**
     * User reached to the end of the list.
     */
    @Override
    public void onItemAtEndLoaded(@NonNull Thread itemAtEnd) {
        appExecutors
                .diskIO()
                .execute(
                        () -> {
                            Pagination current =
                                    db.paginationDao()
                                            .findLoadResult(
                                                    PaginationUtils.prepareThreadKey(query),
                                                    Pagination.RESOURCE_TYPE_THREAD);
                            if (current != null) {
                                int nextPage = current.getCurrentPage() + 1;
                                if (nextPage <= current.getLastPage()) {
                                    helper.runIfNotRunning(
                                            PagingRequestHelper.RequestType.AFTER,
                                            request -> {
                                                webservice
                                                        .getThreads(
                                                                nextPage,
                                                                networkPageSize,
                                                                userHelper.getActiveUserId(),
                                                                query.toApiQueryMap())
                                                        .enqueue(new WebserviceCallback(request));
                                            });
                                }
                            } else {
                                onZeroItemsLoaded();
                            }
                        });
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private void insertItemsIntoDb(
            Response<ThreadListResponse> response, PagingRequestHelper.Request.Callback request) {
        appExecutors
                .diskIO()
                .execute(
                        () -> {
                            dataResponseCallback.handleResponse(response.body());
                            request.recordSuccess();
                        });
    }

    public PagingRequestHelper getPagingRequestHelper() {
        return helper;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public interface DataResponseCallback {
        void handleResponse(ThreadListResponse response);
    }

    class WebserviceCallback implements Callback<ThreadListResponse> {

        private final PagingRequestHelper.Request.Callback requestCallback;

        WebserviceCallback(PagingRequestHelper.Request.Callback requestCallback) {
            this.requestCallback = requestCallback;
        }

        @Override
        public void onResponse(
                @NonNull Call<ThreadListResponse> call,
                @NonNull Response<ThreadListResponse> response) {
            insertItemsIntoDb(response, requestCallback);
        }

        @Override
        public void onFailure(@NonNull Call<ThreadListResponse> call, @NonNull Throwable t) {
            requestCallback.recordFailure(t);
        }
    }
}
