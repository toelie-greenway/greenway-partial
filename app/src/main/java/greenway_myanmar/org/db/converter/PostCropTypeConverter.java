package greenway_myanmar.org.db.converter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.Collections;
import java.util.List;

import greenway_myanmar.org.util.StringUtil;

public class PostCropTypeConverter {

    @TypeConverter
    public static List<Integer> stringToIntList(String data) {
        if (TextUtils.isEmpty(data) || data.length() < 3) {
            return Collections.emptyList();
        }
        data = data.substring(1, data.length() - 1);
        return StringUtil.splitToIntegerList(data);
    }

    @TypeConverter
    public static String intListToString(List<Integer> ints) {
        String result = StringUtil.joinIntegerListIntoString(ints);
        if (!TextUtils.isEmpty(result)) {
            result = "," + result + ",";
        }
        return result;
    }

    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if (TextUtils.isEmpty(data) || data.length() < 3) {
            return Collections.emptyList();
        }
        data = data.substring(1, data.length() - 1);
        return StringUtil.splitToStringList(data);
    }

    @TypeConverter
    public static String stringListToString(List<String> data) {
        String result = StringUtil.joinStringListIntoString(data);
        if (!TextUtils.isEmpty(result)) {
            result = "," + result + ",";
        }
        return result;
    }

}
