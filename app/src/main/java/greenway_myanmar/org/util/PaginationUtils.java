package greenway_myanmar.org.util;

import androidx.annotation.NonNull;

import greenway_myanmar.org.ui.threads.ThreadsViewModel;

public class PaginationUtils {
    public static String prepareCommodityPriceKey(
            @NonNull String township, String crop, String filterDate) {
        return township + "~" + crop + "~" + filterDate;
    }

    public static String prepareBookKey(String filterCategory, int filterAuthor) {
        return filterCategory + "~" + filterAuthor;
    }

    public static String preparePostKey(int authorId) {
        return String.valueOf(authorId);
    }

    public static String prepareThreadKey(ThreadsViewModel.Query query) {
        return query.tagId;
    }

    public static String prepareCommentListKey(String itemId) {
        return itemId + "~" + "comments";
    }

    public static String prepareFeedKey() {
        return "feed_list";
    }

    public static String prepareTradingPostKey() {
        return "feed_list";
    }

    public static String prepareAsymtFarmKey() {
        return "asymt_farm_list";
    }

    public static String prepareAsymtSeasonKey(String farmId) {
        return "asymt_season_list~farm~" + farmId;
    }

    public static String prepareAsymtAlertKey(String seasonId) {
        return "asymt_alert_list~season~" + seasonId;
    }

    public static String prepareAssociationNotificationKey(int associationId) {
        return String.valueOf(associationId);
    }

    public static String prepareTvRadioScheduleKey(String filterID) {
        return filterID;
    }

    public static String prepareMediaTypeKey(String media_name, String filterDate) {
        return media_name + "~" + filterDate;
    }

    public static String prepareUsefulContactByStateKey(String stateId) {
        return stateId;
    }

    public static String prepareFarmListKey(String key) {
        return key;
    }

    public static String prepareFarmExpenseListKey(String id) {
        return id;
    }

    public static String preparePurchaseItemListKey(String id) {
        return id;
    }

    public static String prepareInventoriesListKey(String id) {
        return id;
    }

    public static String prepareSeasonEndYieldsListKey(String id) {
        return id;
    }

    public static String prepareSeasonListKey(String id) {
        return id;
    }

    public static String prepareOrderTransactionListKey(String key) {
        return key;
    }
}
