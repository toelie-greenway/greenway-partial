package greenway_myanmar.org.db.converter;

import androidx.room.TypeConverter;

import java.util.Collections;
import java.util.List;

import greenway_myanmar.org.util.StringUtil;

public class IntegerListConverter {

    @TypeConverter
    public static List<Integer> stringToLongList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return StringUtil.splitToIntegerList(data);
    }

    @TypeConverter
    public static String integerListToString(List<Integer> ints) {
        return StringUtil.joinIntegerListIntoString(ints);
    }
}
