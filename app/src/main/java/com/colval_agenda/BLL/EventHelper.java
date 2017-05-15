package com.colval_agenda.BLL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.colval_agenda.Utils.Preferences;

public class EventHelper extends SQLiteOpenHelper {




    public EventHelper(Context context) {
        super(context, Preferences.DB_NAME, null, Preferences.DB_VERSION);
    }

    // SQLite n'a pas de booléen, utiliser un Int. 0 = False , 1 = True

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE" + Preferences.TB_EVENT + "(" +
                Preferences.EVENT_ID + " integer primary key autoincrement," +
                Preferences.EVENT_TITLE + " text not null," +
                Preferences.EVENT_DESCRIPTION + " text," +
                Preferences.EVENT_LOCATION + " text," +
                Preferences.EVENT_COLOR + " integer," +
                Preferences.EVENT_STARTDATE + " text," +
                Preferences.EVENT_ENDDATE + " text," +
                Preferences.EVENT_REMINDER + " integer" +
                Preferences.EVENT_ALL_DAY + " integer" +
                Preferences.EVENT_EDITABLE + " integer" +
                Preferences.EVENT_USER_ID + " integer" +
                        ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + Preferences.TB_EVENT);
        onCreate(db);
    }
}