package util;

import java.util.Calendar;


public class Utils {

    public static Calendar today() {
        return Calendar.getInstance();
    }

    public static String getTimestamp(Calendar date) {
        int year = date.get(Calendar.YEAR);
        //Note: Januar ~ 0
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);

        return String.format("%d-%02d-%02d", year, month, day);
    }
}
