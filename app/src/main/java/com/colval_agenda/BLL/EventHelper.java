package com.colval_agenda.BLL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventHelper extends SQLiteOpenHelper {

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


    public EventHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // SQLite n'a pas de booléen, utiliser un Int. 0 = False , 1 = True

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE" + TB_EVENT + "(" +
                        EVENT_ID + " integer primary key autoincrement," +
                        EVENT_TITLE + " text not null," +
                        EVENT_DESCRIPTION + " text," +
                        EVENT_LOCATION + " text," +
                        EVENT_COLOR + " integer," +
                        EVENT_STARTDATE + " text," +
                        EVENT_ENDDATE + " text," +
                        EVENT_REMINDER + " integer" +
                        ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TB_EVENT);
        onCreate(db);
    }
}