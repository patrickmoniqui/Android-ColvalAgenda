package com.colval_agenda.BLL;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by macbookpro on 17-05-03.
 */

public class Utils {

    //Convert Date to Calendar
    public static Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    //Convert Calendar to Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public static String padLeft(String str, int count, String padding)
    {
        return String.format("%" + padding + count + "d", str);
    }

    public static String padLeft(int str, String padding, int count)
    {
        return String.format("%" + padding + count + "d", str);
    }

    public static java.util.Date parseDate(String format, String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try
        {
            return sdf.parse(date);
        }
        catch (Exception e) {}
        return null;
    }

    public static Time parseTime(String format, String time)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try
        {
            return new Time(simpleDateFormat.parse(time).getTime());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static Date addTimeToDate(Date date, Time time)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, time.getHours());
        calendar.add(Calendar.MINUTE, time.getMinutes());
        calendar.add(Calendar.SECOND, time.getSeconds());

        return calendarToDate(calendar);
    }
}
