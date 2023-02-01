package greenway_myanmar.org.db.converter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

public class BigDecimalStringConverter {

    @TypeConverter
    public static String fromBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : bigDecimal.toPlainString();
    }

    @TypeConverter
    public static BigDecimal stringToBigDecimal(String bigDecimal) {
        return TextUtils.isEmpty(bigDecimal) ? null : new BigDecimal(bigDecimal);
    }
}
