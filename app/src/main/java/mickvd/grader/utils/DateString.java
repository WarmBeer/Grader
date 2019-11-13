package mickvd.grader.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateString {

    public static String getDateString(Date dt) {
        DateFormat dateFormat = new SimpleDateFormat("EEEEEEE, d MMM");
        return dateFormat.format(dt);
    }

    public static String getCurrentDateString(int pd) {
        Date dt = new Date();
        DateFormat dateFormat = new SimpleDateFormat("EEEEEEE, d MMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DAY_OF_YEAR, pd);
        return dateFormat.format(cal.getTime());
    }
}
