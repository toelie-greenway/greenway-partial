package greenway_myanmar.org.db.converter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import greenway_myanmar.org.util.FloatUnit;

public class FloatUnitConverter {

    @TypeConverter
    public static String fromFloatUnit(FloatUnit intUnit) {
        Gson gson = new Gson();
        return intUnit == null ? null : gson.toJson(intUnit);
    }

    @TypeConverter
    public static FloatUnit toFloatUnit(String jsonString) {
        Gson gson = new Gson();
        return TextUtils.isEmpty(jsonString) ? null : gson.fromJson(jsonString, FloatUnit.class);
    }
}
