package greenway_myanmar.org.db.converter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.UUID;

public class UUIDStringConverter {

    @TypeConverter
    public static String fromUUID(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    @TypeConverter
    public static UUID stringToUUID(String uuid) {
        return TextUtils.isEmpty(uuid) ? null : UUID.fromString(uuid);
    }
}
