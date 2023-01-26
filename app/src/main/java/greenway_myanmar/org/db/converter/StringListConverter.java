package greenway_myanmar.org.db.converter;

import androidx.room.TypeConverter;

import java.util.Collections;
import java.util.List;

import greenway_myanmar.org.util.StringUtil;

public class StringListConverter {

    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return StringUtil.splitToStringList(data);
    }

    @TypeConverter
    public static String stringListToString(List<String> strings) {
        return StringUtil.joinStringListIntoString(strings);
    }
}
