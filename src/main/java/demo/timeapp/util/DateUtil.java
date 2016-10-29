package demo.timeapp.util;

import demo.timeapp.dto.Day;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dhval on 4/10/16.
 */
public class DateUtil {

    private static final DateFormat dateFormatYYMMDD = new SimpleDateFormat("dd/MM/yyyy");

    static {
        dateFormatYYMMDD.setLenient(false);
    }

    public static Date firstDayOfYear() {
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.MONTH,0);
        c.set(Calendar.DAY_OF_MONTH,1);
        return c.getTime();
    }

    public static Date firstDayofWeek() {
        Calendar c = GregorianCalendar.getInstance(Locale.US);
        return  findSunday(c).getTime();
    }

    private static Calendar findSunday(Calendar c) {
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            c.add(Calendar.DATE, -1);
        }
        return c;
    }

    public static Calendar findSunday(Date dt) {
        Calendar c = GregorianCalendar.getInstance(Locale.US);
        c.setTime(dt);
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            c.add(Calendar.DATE, -1);
        }

        return c;
    }

    public static Date findLastSunday() {
        Calendar c = GregorianCalendar.getInstance(Locale.US);
        // Even on Monday we want to go back full one week.
        c.add(Calendar.DATE, -2);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            c.add(Calendar.DATE, -1);
        }
        return c.getTime();
    }

    public static Date findDay(Date endOfWeek, Day day) {
        Calendar c = GregorianCalendar.getInstance(Locale.US);
        c.setTime(endOfWeek);
        while (c.get(Calendar.DAY_OF_WEEK) != day.ordinal()) {
            c.add(Calendar.DATE, -1);
        }

        return c.getTime();
    }

    public static Date today() {
        Calendar c = GregorianCalendar.getInstance(Locale.US);
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }

    private static String formatDate(DateFormat format, Date date) {
        if (date != null) {
            return format.format(date);
        } else {
            return "";
        }
    }

    public static String dateYYMMDD(Date date) {
        return formatDate(dateFormatYYMMDD, date);
    }

    public static List<String> getWeekDays(Date date) {
        List<String> list = new ArrayList<>();
        Calendar c = GregorianCalendar.getInstance(Locale.US);
        c.setTime(date);
        for(int i=0; i<7; i++, c.add(Calendar.DATE, +1)) {
            list.add(formatDate(new SimpleDateFormat("EEE, dd/MM"), c.getTime()));
        }
        return list;
    }
}
