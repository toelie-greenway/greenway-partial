package greenway_myanmar.org.db.converter;

import android.net.Uri;
import android.text.TextUtils;

import androidx.room.TypeConverter;

public class UriStringConverter {

    @TypeConverter
    public static String fromUri(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    @TypeConverter
    public static Uri stringToUri(String uriString) {
        return TextUtils.isEmpty(uriString) ? null : Uri.parse(uriString);
    }
}
