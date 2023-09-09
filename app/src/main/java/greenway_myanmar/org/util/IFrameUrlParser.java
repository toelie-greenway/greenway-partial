package greenway_myanmar.org.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IFrameUrlParser {
    public static String chunkYoutubeUrl(String data) {
        Pattern dataPattern = Pattern.compile("(?<=src=\").[^\"]+");
        Matcher matcher = dataPattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
