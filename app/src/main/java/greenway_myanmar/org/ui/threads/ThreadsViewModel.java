package greenway_myanmar.org.ui.threads;

import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import greenway_myanmar.org.AppExecutors;
import greenway_myanmar.org.db.GreenWayDb;
import greenway_myanmar.org.repository.ThreadRepository;
import greenway_myanmar.org.vo.Listing;
import greenway_myanmar.org.vo.NetworkState;
import greenway_myanmar.org.vo.Thread;

@HiltViewModel
public class ThreadsViewModel extends ViewModel {

    private final MutableLiveData<Listing<Thread>> result = new MutableLiveData<>();
    private final ThreadRepository threadRepository;
    private final LiveData<PagedList<Thread>> threads;
    private final LiveData<NetworkState> networkState;
    private final LiveData<NetworkState> refreshState;

    private final MutableLiveData<Query> query;

    @Inject
    public ThreadsViewModel(
            ThreadRepository threadRepository,
            GreenWayDb db,
            AppExecutors appExecutors) {
        query = new MutableLiveData<>(Query.empty());
        this.threadRepository = threadRepository;
        threads = Transformations.switchMap(result, Listing::getPagedList);
        networkState = Transformations.switchMap(result, Listing::getNetworkState);
        refreshState = Transformations.switchMap(result, Listing::getRefreshState);
    }

    public void start(boolean hasLimit) {
        query.setValue(Query.empty());
            result.setValue(this.threadRepository.loadThreads(Query.empty()));
    }

    public void resetFilter() {
        this.query.setValue(Query.empty());
        result.setValue(threadRepository.loadThreads(Query.empty()));
    }

    public LiveData<PagedList<Thread>> getThreads() {
        return threads;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<NetworkState> getRefreshState() {
        return refreshState;
    }

    public void refresh() {
        if (result.getValue() != null) {
            result.getValue().getRefreshCallback().refresh();
        }
    }

    public void retry() {
        if (result.getValue() != null) {
            result.getValue().getRetryCallback().retry();
        }
    }

    public static class Query {
        public final String tagId;

        public Query(String tagId) {
            this.tagId = tagId;
        }

        public static Query empty() {
            return new Query("");
        }

        public Boolean isEmpty() {
            return TextUtils.isEmpty(tagId);
        }

        private Map<String, String> toFilterMap() {
            ArrayMap<String, String> filterMap = new ArrayMap<>();
            filterMap.put("crop_id", "65");
            return filterMap;
        }

        public Map<String, String> toApiQueryMap() {
            return toFilterMap();
        }

        public Map<String, String> toDbQueryMap() {
            Map<String, String> map = toFilterMap();

//            if (subcategory == null && categoryId != null && categoryId != CategoryID.UNKNOWN) {
//                map.remove("category_id");
//                map.put("category_parentId", String.valueOf(categoryId.getId()));
//            }

            return map;
        }
    }
}
