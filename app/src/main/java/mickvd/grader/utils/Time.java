package mickvd.grader.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Time {

    private static TimeZone timeZone = TimeZone.getTimeZone("GMT+2");
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static String getTime(Date date) {
        timeFormat.setTimeZone(timeZone);
        return timeFormat.format(date);
    }
}
