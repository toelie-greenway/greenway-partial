package greenway_myanmar.org.db.converter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import greenway_myanmar.org.util.IntUnit;

public class IntUnitConverter {

    @TypeConverter
    public static String fromIntUnit(IntUnit intUnit) {
        Gson gson = new Gson();
        return intUnit == null ? null : gson.toJson(intUnit);
    }

    @TypeConverter
    public static IntUnit toIntUnit(String jsonString) {
        Gson gson = new Gson();
        return TextUtils.isEmpty(jsonString) ? null : gson.fromJson(jsonString, IntUnit.class);
    }
}
