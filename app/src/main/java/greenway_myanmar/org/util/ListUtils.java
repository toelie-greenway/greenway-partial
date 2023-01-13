package greenway_myanmar.org.util;

import java.util.List;

public class ListUtils {

    public static boolean isEmpty(Object obj) {
        if (obj instanceof List) {
            List list = (List) obj;
            return list.isEmpty();
        }
        return obj == null;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }
}
