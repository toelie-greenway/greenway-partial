package greenway_myanmar.org.util;

import android.text.TextUtils;

import androidx.core.os.LocaleListCompat;

import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyanmarZarConverter {

    public static Locale getLocale() {
        try {
            return new Locale("my");
        } catch (Exception e) {
            return LocaleListCompat.getDefault().get(0);
        }
    }

    public static String toMyanmarNumber(int number) {
        return toMyanmarNumber(String.format(Locale.US, "%,d", number));
    }

    public static String toMyanmarNumber(double number) {
        NumberFormat df = DecimalFormat.getInstance();
        return toMyanmarNumber(df.format(number));
    }

    public static String toMyanmarNumber(float number) {
        NumberFormat df = DecimalFormat.getInstance();
        return toMyanmarNumber(df.format(number));
    }

    public static String toMyanmarNumber(String numberString) {
        if (TextUtils.isEmpty(numberString)) {
            return "";
        }

        StringBuilder resultNumber = new StringBuilder();
        for (int i = 0, length = numberString.length(); i < length; i++) {
            char c = numberString.charAt(i);
            if (c >= '0' && c <= '9') {
                resultNumber.append((char) (c + 0x1010));
            } else {
                resultNumber.append(c);
            }
        }
        return resultNumber.toString();
    }

    public static String getIntrinsicValue(String myanmarNumberString) {
        if (TextUtils.isEmpty(myanmarNumberString)) return "";

        StringBuilder resultNumber = new StringBuilder();
        for (int i = 0, length = myanmarNumberString.length(); i < length; i++) {
            char c = myanmarNumberString.charAt(i);
            if (c >= '၀' && c <= '၉') {
                resultNumber.append((char) (c - 0x1010));
            } else {
                resultNumber.append(c);
            }
        }
        return resultNumber.toString();
    }

    public static int getIntrinsicValueOrThrow(String myanmarNumberString) {
        if (TextUtils.isEmpty(myanmarNumberString)) return 0;

        StringBuilder resultNumber = new StringBuilder();
        for (int i = 0, length = myanmarNumberString.length(); i < length; i++) {
            char c = myanmarNumberString.charAt(i);
            if (c >= '၀' && c <= '၉') {
                resultNumber.append((char) (c - 0x1010));
            } else {
                throw new InvalidParameterException("Number contained char that is not number!");
            }
        }
        return Integer.parseInt(resultNumber.toString());
    }

    public static boolean isMyanmarNumber(int codePoint) {
        return codePoint >= '၀' && codePoint <= '၉';
    }

    public static String cropPriceToMyanmarNumber(String numberString) {
        StringBuilder resultNumber = new StringBuilder();
        for (int i = 0, length = numberString.length(); i < length; i++) {
            char c = numberString.charAt(i);
            if (c >= '0' && c <= '9') {
                resultNumber.append((char) (c + 0x1010));
            } else if (c == '-') {
                resultNumber.append(" မှ ");
            } else {
                resultNumber.append(c);
            }
        }
        return resultNumber.toString();
    }

    public static String toMyanmarPrice(double number) {
        DecimalFormat df = new DecimalFormat("0.#");
        return toMyanmarPrice(df.format(number));
    }

    public static String toMyanmarPrice(int number) {
        // return toMyanmarPrice(String.valueOf(number));
        return toMyanmarPrice(String.format(Locale.US, "%,d", number));
    }

    public static String toMyanmarPrice(int number, boolean commaSeparated) {
        return toMyanmarPrice(commaSeparated ? String.format(Locale.US, "%,d", number) : String.valueOf(number));
    }

    public static String toMyanmarPrice(String numberString) {
        StringBuilder resultNumber = new StringBuilder();
        for (int i = 0, length = numberString.length(); i < length; i++) {
            char c = numberString.charAt(i);
            if (c >= '0' && c <= '9') {
                resultNumber.append((char) (c + 0x1010));
            } else {
                resultNumber.append(c);
            }
        }
        return resultNumber + " ကျပ်";
    }

    public static String toMyanmarAca(String numberString) {
        if (TextUtils.isEmpty(numberString)) return "";

        StringBuilder resultNumber = new StringBuilder();
        for (int i = 0, length = numberString.length(); i < length; i++) {
            char c = numberString.charAt(i);
            if (c >= '0' && c <= '9') {
                resultNumber.append((char) (c + 0x1010));
            } else {
                resultNumber.append(c);
            }
        }
        return resultNumber + " ဧက";
    }

    public static String toMyanmarAcre(double acre) {
        return toMyanmarNumber(acre) + " ဧက";
    }

    /***
     * Get English Month Name in Myanmar Language
     *
     * @param month the same as Java month, Start from 0
     * @return
     */
    public static String toEnglishMonthInMyanmar(int month) {
        switch (month) {
            case 0:
                return "ဇန်နဝါရီ";
            case 1:
                return "ဖေဖော်ဝါရီ";
            case 2:
                return "မတ်";
            case 3:
                return "ဧပြီ";
            case 4:
                return "မေ";
            case 5:
                return "ဇွန်";
            case 6:
                return "ဇူလိုင်";
            case 7:
                return "သြဂုတ်";
            case 8:
                return "စက်တင်ဘာ";
            case 9:
                return "အောက်တိုဘာ";
            case 10:
                return "နိုဝင်ဘာ";
            case 11:
                return "ဒီဇင်ဘာ";
        }
        return "Invalid Month; Notice: index is started from zero(0)";
    }

    public static String getUserFriendlyDateInMyanmar(int years, int monthOfYear, int dayOfMonth, boolean shorten) {
        if (shorten) {
            return toEnglishMonthInMyanmar(monthOfYear) + " " + toMyanmarNumber(dayOfMonth) + "၊ " + toMyanmarNumber(years);
        } else {
            return toEnglishMonthInMyanmar(monthOfYear) + "လ " + toMyanmarNumber(dayOfMonth) + " ရက်၊ " + toMyanmarNumber(years);
        }
    }

    public static String getUserFriendlyDateInMyanmar(Date date, boolean shorten) {
        if (date == null) return "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return getUserFriendlyDateInMyanmar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), shorten);
    }

    public static String getUserFriendlyMondayDayInMyanmar(Date date, boolean shorten) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        if (shorten) {
            return toEnglishMonthInMyanmar(calendar.get(Calendar.MONTH)) + " " + toMyanmarNumber(calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            return toEnglishMonthInMyanmar(calendar.get(Calendar.MONTH)) + "လ " + toMyanmarNumber(calendar.get(Calendar.DAY_OF_MONTH) + " ရက်");
        }
    }

    public static String convertToMyanmarUnit(String unitInEnglish) {
        if ("Acre".equalsIgnoreCase(unitInEnglish)) {
            return "ဧက";
        }
        return "";
    }

}
