package com.colval_agenda.BLL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.colval_agenda.LoginActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by macbookpro on 17-05-03.
 */

public class Utils {

    public static void RegisterGlobalUser(Context ctx, int id, String password)
    {
        SharedPreferences settings = ctx.getApplicationContext().getSharedPreferences("Login", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("userId", id);
        editor.putString("userPwd", password);
        editor.apply();
    }

    public static void UnregisterGlobalUser(Context ctx)
    {
        SharedPreferences settings = ctx.getApplicationContext().getSharedPreferences("Login", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("userId");
        editor.remove("userPwd");
    }

    public static boolean GlobalUserLogged(Context ctx)
    {
        SharedPreferences settings = ctx.getApplicationContext().getSharedPreferences("Login", 0);
        int id = settings.getInt("userId", -1);
        String pwd = settings.getString("userPwd", "");

        if(id != -1 && !pwd.isEmpty()) return true;
        else return false;
    }

    public static int GetGlobalUserId(Context ctx)
    {
        SharedPreferences settings = ctx.getApplicationContext().getSharedPreferences("Login", 0);
        int id = settings.getInt("userId", -1);

        return id;
    }

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
