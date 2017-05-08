package com.colval_agenda.BLL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 0756992 on 2017-04-24.
 */
public class EventManager {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    SQLiteOpenHelper helper;
    SQLiteDatabase database;

    public EventManager(Context context) {
        helper = new EventHelper(context);
        database = helper.getWritableDatabase();
    }

    public void open(){
        if (!database.isOpen()){
            database = helper.getWritableDatabase();
        }
    }

    public void close(){
        if (database.isOpen()){
            database.close();
        }
    }

    // SQLite n'a pas de boolean, utiliser un Int. 0 = False , 1 = True

    public void insertEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(EventHelper.EVENT_TITLE, event.getTitle());
        values.put(EventHelper.EVENT_DESCRIPTION, event.getDescription());
        values.put(EventHelper.EVENT_LOCATION, event.getTitle());
        values.put(EventHelper.EVENT_COLOR, event.getTitle());
        values.put(EventHelper.EVENT_STARTDATE, dateFormat.format(event.getStartDate()));
        values.put(EventHelper.EVENT_ENDDATE, dateFormat.format(event.getFinishDate()));
        values.put(EventHelper.EVENT_REMINDER, booleanToInt(event.isReminder()));
        database.insert(EventHelper.TB_EVENT, null, values);
    }

    public void updateEvent (Event event){
        ContentValues values = new ContentValues();
        values.put(EventHelper.EVENT_TITLE, event.getTitle());
        values.put(EventHelper.EVENT_DESCRIPTION, event.getDescription());
        values.put(EventHelper.EVENT_LOCATION, event.getTitle());
        values.put(EventHelper.EVENT_COLOR, event.getTitle());
        values.put(EventHelper.EVENT_STARTDATE, dateFormat.format(event.getStartDate()));
        values.put(EventHelper.EVENT_ENDDATE, dateFormat.format(event.getFinishDate()));
        values.put(EventHelper.EVENT_REMINDER, booleanToInt(event.isReminder()));

        String [] id = new String[1];
        id[0] = event.getId()+"";
        database.update(EventHelper.TB_EVENT, values, EventHelper.EVENT_ID + "=?", id);
    }

    public void deleteEvent (Event event){
        String[] values = new String[1];
        values[0] = event.getId()+"";
        database.delete(EventHelper.TB_EVENT, EventHelper.EVENT_ID + "=?", values);
    }

    public List<Event> getAllEvents() throws ParseException {
        Cursor cursor = database.query(EventHelper.TB_EVENT, null,null,null,null,null,null);
        List<Event> events = new ArrayList<>();
        if (cursor != null){
            cursor.moveToFirst();
            do{
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex(EventHelper.EVENT_ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_TITLE)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_DESCRIPTION)));
                event.setLocation(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_LOCATION)));
                event.setEventColor(cursor.getInt(cursor.getColumnIndex(EventHelper.EVENT_COLOR)));
                try {
                    event.setStartDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_STARTDATE))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    event.setFinishDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_ENDDATE))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                event.setReminder(intToBoolean(cursor.getInt(cursor.getColumnIndex(EventHelper.EVENT_REMINDER))));

                events.add(event);
            }while (cursor.moveToNext());
        }
        return events;
    }

    public Event getEventById(int id){
        String [] values = new String[1];
        values[0] = id+"";
        Event event = null;
        Cursor cursor = database.query(EventHelper.TB_EVENT, null, "id=?", values, null,null,null);
        if (cursor !=null){
            cursor.moveToFirst();
            event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndex(EventHelper.EVENT_ID)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_TITLE)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_DESCRIPTION)));
            event.setLocation(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_LOCATION)));
            event.setEventColor(cursor.getInt(cursor.getColumnIndex(EventHelper.EVENT_COLOR)));
            try {
                event.setStartDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_STARTDATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                event.setFinishDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(EventHelper.EVENT_ENDDATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            event.setReminder(intToBoolean(cursor.getInt(cursor.getColumnIndex(EventHelper.EVENT_REMINDER))));
        }
        return event;
    }

    public int booleanToInt (boolean reminder){
        if(reminder) return 1;
        else return 0;
    }

    public boolean intToBoolean (int reminder){
        switch (reminder){
            case 0: return false;
            case 1: return true;
            default: return false;
        }
    }
}
