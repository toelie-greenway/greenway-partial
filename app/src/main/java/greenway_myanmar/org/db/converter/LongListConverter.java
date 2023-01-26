package greenway_myanmar.org.db.converter;

import androidx.room.TypeConverter;

import java.util.Collections;
import java.util.List;

import greenway_myanmar.org.util.StringUtil;

public class LongListConverter {

    @TypeConverter
    public static List<Long> stringToLongList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return StringUtil.splitToLongList(data);
    }

    @TypeConverter
    public static String longListToString(List<Long> longs) {
        return StringUtil.joinLongListIntoString(longs);
    }
}
