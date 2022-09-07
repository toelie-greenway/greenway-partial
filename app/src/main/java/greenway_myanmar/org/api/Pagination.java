package greenway_myanmar.org.api;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Pagination {

    public static final String DEFAULT_KEY = "-1";
    public static final String RESOURCE_TYPE_POST = "post";
    public static final String RESOURCE_TYPE_BOOK = "book";
    public static final String RESOURCE_TYPE_COMMODITY_PRICE = "commodity_price";
    public static final String RESOURCE_TYPE_TV_RADIO_SCHEDULE = "media";
    public static final String RESOURCE_TYPE_THREAD = "thread";
    public static final String RESOURCE_TYPE_ASSOCIATION_NOTIFICATION = "association_notification";
    public static final String RESOURCE_TYPE_USEFUL_CONTACT = "useful_contact";
    public static final String RESOURCE_TYPE_CONTENT_PROVIDER = "content_provider";
    public static final String RESOURCE_TYPE_FARM = "farm";
    public static final String RESOURCE_TYPE_SEASON = "season";
    public static final String RESOURCE_TYPE_SEASON_END_YIELD = "yield";
    public static final String RESOURCE_TYPE_FARM_EXPENSE = "farm_expense";
    public static final String RESOURCE_TYPE_ORDER_TRANSACTION = "order_transaction";
    public static final String RESOURCE_TYPE_FISHING_RECORD = "fishing_record";
    public static final String RESOURCE_TYPE_FEED = "feed";
    public static final String RESOURCE_TYPE_TRADING_POST = "trading_post";
    public static final String RESOURCE_TYPE_THREAD_COMMENT = "thread_comment";
    public static final String RESOURCE_TYPE_TRADING_POST_COMMENT = "trading_post_comment";
    public static final String RESOURCE_TYPE_POST_COMMENT = "post_comment";
    public static final String RESOURCE_TYPE_AD_COMMENT = "ad_comment";
    public static final String RESOURCE_TYPE_ASYMT_FARM = "asymt_farm";
    public static final String RESOURCE_TYPE_ASYMT_SEASON = "asymt_season";
    public static final String RESOURCE_TYPE_ASYMT_ALERT = "asymt_alert";
    public static final String RESOURCE_TYPE_ASYMT_FARM_INPUT_PRODUCT = "asymt_farm_input_product";
    @NonNull
    private String id;
    @NonNull
    private String resourceType;
    @SerializedName("total")
    private int total;
    @SerializedName("per_page")
    private int perPage;
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("last_page")
    private int lastPage;
    @SerializedName("from")
    private int from;
    @SerializedName("to")
    private int to;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(@NonNull String resourceType) {
        this.resourceType = resourceType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({RESOURCE_TYPE_POST, RESOURCE_TYPE_BOOK, RESOURCE_TYPE_COMMODITY_PRICE})
    public @interface ResourceType {
    }

}
