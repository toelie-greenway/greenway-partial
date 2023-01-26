package greenway_myanmar.org.util;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {

    /**
     * Splits a comma separated list of integers to integer list.
     * <p>
     * If an input is malformed, it is omitted from the result.
     *
     * @param input Comma separated list of integers.
     * @return A List containing the integers or null if the input is null.
     */
    @Nullable
    public static List<String> splitToStringList(@Nullable String input) {
        if (input == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, ",");
        while (tokenizer.hasMoreElements()) {
            final String item = tokenizer.nextToken();
            result.add(item);
        }
        return result;
    }

    /**
     * Splits a comma separated list of integers to integer list.
     * <p>
     * If an input is malformed, it is omitted from the result.
     *
     * @param input Comma separated list of integers.
     * @return A List containing the integers or null if the input is null.
     */
    @Nullable
    public static List<Integer> splitToIntegerList(@Nullable String input) {
        if (input == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, ",");
        while (tokenizer.hasMoreElements()) {
            final String item = tokenizer.nextToken();
            result.add(Integer.parseInt(item));
        }
        return result;
    }

    /**
     * Joins the given list of strings into a comma separated list.
     *
     * @param input The list of strings.
     * @return Comma separated string composed of strings in the list. If the list is null, return
     * value is null.
     */
    @Nullable
    public static String joinStringListIntoString(@Nullable List<String> input) {
        if (input == null) {
            return null;
        }

        final int size = input.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(input.get(i));
            if (i < size - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Joins the given list of strings into a comma separated list.
     *
     * @param input The list of strings.
     * @return Comma separated string composed of strings in the list. If the list is null, return
     * value is null.
     */
    @Nullable
    public static String joinIntegerListIntoString(@Nullable List<Integer> input) {
        if (input == null) {
            return null;
        }

        final int size = input.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(input.get(i));
            if (i < size - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }


    /**
     * Splits a comma separated list of longs to long list.
     * <p>
     * If an input is malformed, it is omitted from the result.
     *
     * @param input Comma separated list of integers.
     * @return A List containing the integers or null if the input is null.
     */
    @Nullable
    public static List<Long> splitToLongList(@Nullable String input) {
        if (input == null) {
            return null;
        }
        List<Long> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, ",");
        while (tokenizer.hasMoreElements()) {
            final String item = tokenizer.nextToken();
            result.add(Long.parseLong(item));
        }
        return result;
    }

    /**
     * Joins the given list of longs into a comma separated list.
     *
     * @param input The list of longs.
     * @return Comma separated string composed of strings in the list. If the list is null, return
     * value is null.
     */
    @Nullable
    public static String joinLongListIntoString(@Nullable List<Long> input) {
        if (input == null) {
            return null;
        }

        final int size = input.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(input.get(i));
            if (i < size - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
