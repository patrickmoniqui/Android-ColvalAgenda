package com.colval_agenda.DAL;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;

import com.colval_agenda.BLL.Event;
import com.colval_agenda.BLL.EventManager;
import com.colval_agenda.Utils.Utils;
import com.colval_agenda.Notifications;
import com.github.tibolte.colvalcalendar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by macbookpro on 17-05-03.
 */

public class EventRepository {
    private static EventRepository instance = null;
    private Context context;
    List<Event> eventList;
    EventManager manager;
    private static int mId = 1;
    LoginManager lm;

    protected EventRepository(Context ctx) {
        context = ctx;
        manager = new EventManager(context);
    }

    public static EventRepository getInstance(Context ctx) {
        if(instance == null) {
            instance = new EventRepository(ctx);
        }
        return instance;
    }

    public Event GetEventById(int id)
    {
        return manager.getEventById(id);
    }

    public List<Event> GetAllEventsByUserId(int id)
    {
        return manager.GetEventsForUser(id);
    }

    public List<Event> GetAll(int id)
    {
        return manager.getAllEvents();
    }

    public boolean AddEvent(Event event)
    {
        event.setUserId(Utils.GetGlobalUserId(context));
        event.setEventColor(ResourcesCompat.getColor(context.getResources(), R.color.dark_blue, null));

        manager.insertEvent(event);

        if(event.isReminder())
        {
            Notifications n = new Notifications(context);
            n.createNotificationAtDate(event.getId(), event.getStartDate(), "Colval Agenda", "Votre événement " + event.getTitle() + " commence!");
        }

        return true;
    }

    public boolean DeleteEvent(int id)
    {

        manager.deleteEvent(id);

        Notifications n = new Notifications(context);
        n.deleteNotification(id);

        return true;
    }

    public boolean EditEvent(int id, Event event) {
        try {
            boolean notificationBefore = manager.getEventById(id).isReminder();

            event.setUserId(Utils.GetGlobalUserId(context));

            manager.updateEvent(id, event);

            if (notificationBefore == false && event.isReminder()) {
                Notifications n = new Notifications(context);
                n.createNotificationAtDate(event.getId(), event.getStartDate(), "Colval Agenda", "Votre événement " + event.getTitle() + " commence!");
            } else if (notificationBefore && event.isEditable() == false) {
                Notifications n = new Notifications(context);
                n.deleteNotification(event.getId());
            }
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}