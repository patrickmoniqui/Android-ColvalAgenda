package com.colval_agenda.Utils;

/**
 * Created by Shadow on 15/05/2017.
 */

public class Preferences {

    public static final String DB_NAME = "Name";
    public static final int DB_VERSION = 1;
    public static final String TB_EVENT = "events";
    public static final String EVENT_ID = "id";
    public static final String EVENT_TITLE = "title";
    public static final String EVENT_DESCRIPTION = "description";
    public static final String EVENT_LOCATION = "location";
    public static final String EVENT_COLOR = "color";
    public static final String EVENT_STARTDATE = "startDate";
    public static final String EVENT_ENDDATE = "endDate";
    public static final String EVENT_REMINDER = "reminder";
    public static final String EVENT_ALL_DAY = "allday";
    public static final String EVENT_EDITABLE = "editable";
    public static final String EVENT_USER_ID = "user_id";

    public static String URL_CHECK_LOGIN = "http://10.0.2.2:60705/AndroidService.asmx?op=checkLogin";
    public static String URL_GET_CLASSES = "http://10.0.2.2:60705/AndroidService.asmx/getClasses";
}
