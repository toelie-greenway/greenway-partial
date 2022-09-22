package greenway_myanmar.org.util;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import greenway_myanmar.org.BuildConfig;

public class CommonConstants {

    public static final int DEFAULT_PAGE_SIZE = 15;

    // Constant used in Firebase Dynamic Links
    public static final String FIREBASE_DYNAMIC_LINK_PATH_BOOK = "books";
    public static final String FIREBASE_DYNAMIC_LINK_PATH_POST = "posts";

    // category parent id
    public static final String CATEGORY_PARENT_ID_AGRI_TECH = "1";
    public static final String CATEGORY_PARENT_ID_KNOWLEDGE = "2";
    public static final String CATEGORY_PARENT_ID_NEWS = "3";
    public static final String CATEGORY_PARENT_ID_FISHERY = "41";
    public static final String CATEGORY_PARENT_ID_LIVESTOCK = "45";

    // category parent title
    public static final String CATEGORY_PARENT_TITLE_AGRI_TECH = "စိုက်ပျိုးရေးနည်းပညာ";
    public static final String CATEGORY_PARENT_TITLE_KNOWLEDGE = "ဗဟုသုတ";
    public static final String CATEGORY_PARENT_TITLE_KNOWLEDGE_LONG = "အထွေထွေ ဗဟုသုတ";
    public static final String CATEGORY_PARENT_TITLE_NEWS = "သတင်း";
    public static final String CATEGORY_PARENT_TITLE_LIVESTOCK_TECH = "မွေးမြူနည်းပညာ";
    public static final String CATEGORY_PARENT_TITLE_FISHERY = "ရေလုပ်ငန်းနည်းပညာ";

    public static final String TITLE_USER_SAVED_POST = "သင့်စိတ်ကြိုက်ပိုစ့်များ";

    // category ids
    public static final String CATEGORY_ID_WEATHER = "20";

    // DON'T MODIFY THESE VALUES
    public static final String CAREER_FARMER = "farmer";
    public static final String CAREER_AGRI_TECHNICIAN = "agriculturalist";
    public static final String CAREER_SHOP = "shop";
    public static final String CAREER_TRADER = "trader";
    public static final String CAREER_LIVESTOCK_TECHNICIAN = "livestock";
    public static final String CAREER_FISH_FARMER = "fish_farmer";
    public static final String CAREER_OTHER = "other";
    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";
    public static final String WORKPLACE_GOVERNMENT = "government";
    public static final String WORKPLACE_FREELANCE = "freelance";
    public static final String WORKPLACE_ORGANIZATION = "organization";
    public static final String WORKPLACE_COMPANY = "company";
    public static final String FISH_FARMER_TYPE_FISHERMAN = "fisherman";
    public static final String FISH_FARMER_TYPE_FISH_FARM = "fish_farm";
    public static final String FISH_FARMER_TYPE_BOTH = "both";
    // this index is mapped with xml
    public static final int FISH_FARMER_TYPE_FISHERMAN_INDEX = 0;
    public static final int FISH_FARMER_TYPE_FISH_FARM_INDEX = 1;
    //user questions
    public static final String TITLE_USER_QUESTIONS = "သင့်မေးခွန်းများ";
    public static final String TITLE_THREADS = "အမေး/အဖြေ";
    //CropTypeID Parent id
    public static final String CROPTYPE_PARENT_ID_0 = "0";
    //For user
    public static final String SHARE_PREFERENCE_USER_INFO = "greenway_myanmar.org.user.info";
    public static final String WEATHER_MOBILE_API = "https://m.accuweather.com/en/mm/yangon/246562/current-weather/246562?lang=en-us";
    public static final String ABOUT_US_URL = "https://greenwaymyanmar.com/pages/4";
    // public static final String USER_FCM_TOKE = "user_token";
    public static final String USER_APP_VERSON = "app_version";
    public static final String FCM_TOKEN = "token";
    public static final String FCM_ARTICLES_TOPICS_FOR_ALL = "articles";
    public static final String FCM_MESSAGE_TYPE_1 = "all";
    public static final String FCM_MESSAGE_TYPE_2 = "single";

    /* //TODO: comment out for a while
    public static final String USER_ID= "user_Id";

    public static final String USER_PHONE = "phone";
    public static final String USER_NAME = "name";
    public static final String USER_AGE = "age";
    public static final String USER_GENDER = "gender";

    public static final String USER_STATE_ID = "state_id";
    public static final String USER_TOWNSHIP_ID = "township_id";
    public static final String USER_VTTOWN_ID = "vttown_id";

    public static final String USER_WARD_ID = "ward_id";
    public static final String USER_CAREER = "career";
    public static final String USER_CROP = "crop";

    public static final String USER_WORK_PLACE = "work_place";

    //For Technician
    public static final String USER_UNIYEAR = "uni_year";
    public static final String USER_UNINAME = "uni_name";
    public static final String USER_GOVDEP = "gov_dep";
    public static final String USER_GOVDEP_LIVESTOCk = "gov_dep_livestock";
    public static final String USER_ORGTYPE = "org_type";

    public static final String USER_EXPERT_FIELD = "expert_field";
    public static final String USER_EXPERT_CROPS = "expert_crops";

    public static final String USER_RANK = "rank";
    public static final String USER_COMNAME = "com_name";
    public static final String USER_ORGNAME = "org_name";

    public static final String USER_SHOP_NAME = "shop_name";
    public static final String USER_SHOP_ADDRESS = "shop_address";
    public static final String USER_OTHER_DEPARTMENT = "department";

    public static final String USER_TRADER_NAME = "trader_name";
    public static final String USER_INFO_STATUS = "information_status";
    public static final String FIRST_TIME = "first_time";
    */
    public static final String FCM_MESSAGE_TYPE_3 = "career";
    public static final String FCM_MESSAGE_TYPE_4 = "tea_member_register_user";
    public static final String FCM_MESSAGE_TYPE_5 = "bamboo_member_register_user";
    public static final String FCM_MESSAGE_TYPE_6 = "company_member_tester_farmer";
    public static final String FCM_MESSAGE_TYPE_7 = "questions";
    public static final String FCM_MESSAGE_TYPE_8 = "news";
    public static final String FCM_MESSAGE_ORG_TYPE = "org_type";
    public static final String FCM_MESSAGE_NORMAL_TYPE = "normal_type";
    // growing guide category title
    public static final String GROWING_GUIDE_CATEGORY_PLANTING = "စိုက်ပျိုးနိုင်သည့် အခြေအနေ";
    public static final String GROWING_GUIDE_CATEGORY_CROPANDSEED = "သီးနှံ/မျိုး ရွေးချယ်ခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_FARMING = "မြေပြုပြင်ခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_START_PLANTING = "ပျိုးထောင်ခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_USER_PLANTING = "စိုက်ပျိုးခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_CARING_PLANT = "အပင်ပြုစုခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_FERTILISER = "မြေသြဇာကြွေးခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_IRRIGATION = "ရေသွင်းရေထုတ်";
    public static final String GROWING_GUIDE_CATEGORY_PESTS_DISEASES = "ပိုးမွှားရောဂါနှင့် ဖျက်ပိုးများ";
    public static final String GROWING_GUIDE_CATEGORY_WEEDGRASS = "ပေါင်းကာကွယ်ခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_MOUSE_DEFENSE = "ကြွက်နှင့်အခြားဖျက်ကောင်များ";
    public static final String GROWING_GUIDE_CATEGORY_HARVASTING = "ရိတ်သိမ်းခြင်း";
    public static final String GROWING_GUIDE_CATEGORY_STORING_SELLING = "သိုလှောင်ခြင်း ရောင်းချခြင်း";
    // Latest thread item count shown in Home Screen
    public static final int LATEST_THREAD_COUNT = 7;
    //youtube api for video
    public static final String YOUTUBE_API_KEY = "AIzaSyB2fEeZ39DTQkVUu-FqGV0RCWg0V_NFhts";  //youtube api key in release
    // network error message use in retrofit network call
    public static final String NETWORK_ERROR_MESSAGE = "ချိတ်ဆက်မှု မအောင်မြင်ပါ။ မိတ်ဆွေရဲ့ အင်တာနက်လိုင်းအား စစ်ဆေး၍ ပြန်လည်ကြိုးစားကြည့်ပါ။";
    // group shared preferences key
    public static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    public static final String KEY_GROUP_NAME = "KEY_GROUP_NAME";
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            CAREER_FARMER,
            CAREER_AGRI_TECHNICIAN,
            CAREER_SHOP,
            CAREER_TRADER,
            CAREER_LIVESTOCK_TECHNICIAN,
            CAREER_FISH_FARMER,
            CAREER_OTHER
    })
    public @interface CareerType {
    }
    @StringDef({WORKPLACE_GOVERNMENT, WORKPLACE_FREELANCE, WORKPLACE_ORGANIZATION, WORKPLACE_COMPANY})
    public @interface Workplace {
    }
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            FISH_FARMER_TYPE_FISHERMAN,
            FISH_FARMER_TYPE_FISH_FARM,
            FISH_FARMER_TYPE_BOTH
    })
    public @interface FishFarmerType {
    }

    public static final class UserFriendlyCareerType {
        public static final String FARMER = "တောင်သူလယ်သမား";
        public static final String AGRICULTURALIST = "စိုက်ပျိုးရေးပညာရှင်";
        public static final String SHOP = "အရောင်းဆိုင်";
        public static final String TRADER = "ကုန်သည်/ပွဲရုံ";
        public static final String LIVESTOCK = "မွေးမြူရေးပညာရှင်";
        public static final String FISH_FARMER = "ရေလုပ်သား";
        public static final String OTHER = "အခြား";
    }

    public static final class UserFriendlyGenderType {
        public static final String MALE = "ကျား";
        public static final String FEMALE = "မ";
    }

    public static final class UserFriendlyWorkplace {
        public static final String GOVERNMENT = "အစိုးရ ဌာန";
        public static final String FREELANCE = "အလွတ်တန်း ပညာရှင်";
        public static final String COMPANY = "ကုမ္ပဏီ";
        public static final String ORGANIZATION = "အဖွဲ့အစည်း";
    }

    public static final class NetworkConfig {
        public static final int DEFAULT_NETWORK_PAGE_SIZE = 15;
        public static final int DEFAULT_PAGE_SIZE = 15;
    }

    // constants used in FCM Message feature
    public static final class FcmMessageKey {
        public static final String KEY_JSON = "fcm_json_message";
        public static final String KEY_CONTENT_ID = "id";
        public static final String KEY_CONTENT_TYPE = "type";
        public static final String KEY_TITLE = "title";
        public static final String KEY_TEXT = "message";
        public static final String KEY_LARGE_ICON_URL = "icon_image_url";
        public static final String KEY_BIG_PICTURE_URL = "big_picture_url";
        public static final String KEY_ADS_IMAGE_URL = "ads_image_url";
        public static final String KEY_WEB_LINK = "web_link";

        public static final String KEY_IS_IMAGE = "is_image";
        public static final String KEY_ROOM_ID = "room_id";
        public static final String KEY_ORDER_ID = "order_id";
        public static final String KEY_ROOM_TYPE = "room_type";
        public static final String KEY_COMPANY_NAME = "company_name";

        public static final String KEY_GROUP_ID = "group_id";
        public static final String KEY_GROUP_NAME = "group_name";

    }

    public static final class FcmMessageType {

        public static final String TYPE_POST_COMMENT = "post_comment";
        public static final String TYPE_THREAD_COMMENT = "thread_comment";
        public static final String TYPE_AD_COMMENT = "ad_comment";

        public static final String TYPE_POST_AGRI_TECH = "post_agri_tech";
        public static final String TYPE_POST_KNOWLEDGE = "post_knowledge";
        public static final String TYPE_POST_NEWS = "post_news";
        public static final String TYPE_POST_LIVESTOCK = "post_livestock";

        public static final String TYPE_PUSH_MESSAGE = "push_message";
        public static final String TYPE_COMMODITY_PRICE = "commodity";
        public static final String TYPE_WEATHER = "weather";
        public static final String TYPE_SURVEY = "survey";
        public static final String TYPE_THREAD = "thread";
        public static final String TYPE_BOOK = "book";
        public static final String TYPE_MARKET_PLACE_NEW_ORDER = "new_order";
        public static final String TYPE_MARKET_PLACE_NEW_CIC_ORDER = "new_cic_order";

        public static final String TYPE_ASSOCIATION_NOTICE = "association_notice";
        public static final String TYPE_INPUT_CALCULATOR_INVITATION = "cic_invitation";
        public static final String TYPE_INPUT_CALCULATOR_INVITATION_RESPONSE_PREFIX = "cic_invitation_";
        public static final String TYPE_INPUT_CALCULATOR_INVITATION_RESPONSE_ACCEPTED = "cic_invitation_accepted";
        public static final String TYPE_INPUT_CALCULATOR_INVITATION_RESPONSE_REJECTED = "cic_invitation_rejected";

        public static final String TYPE_CHAT = "CHAT";

        public static final String TYPE_TRADING_POST = "sblinkage";
        public static final String TYPE_TRADING_POST_COMMENT = "sblinkage_comment";

        public static final String TYPE_TECHNICIAN_DAILY_REMINDER = "technician_daily_reminder";
        public static final String TYPE_ASYL_DAILY_REMINDER = "farming_record_daily_reminder";

        public static final String TYPE_GROUP_POST = "group_post";
        public static final String TYPE_GROUP_POST_COMMENT = "group_post_comment";
        public static final String TYPE_GROUP_ANNOUNCEMENT = "group_announcement";
        public static final String TYPE_GROUP_ANNOUNCEMENT_COMMENT = "group_announcement_comment";
    }

    public static final class FormResourceFile {
        public static final String FORM_ASYMT_ADD_EDIT_FARM = "asymt_add_edit_farm_form.html";
    }

}
