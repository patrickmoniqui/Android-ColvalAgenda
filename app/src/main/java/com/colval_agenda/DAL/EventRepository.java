package com.colval_agenda.DAL;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;

import com.colval_agenda.BLL.Event;
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
    private static int mId = 1;
    LoginManager lm;

    protected EventRepository(Context ctx) {
        context = ctx;

        eventList = new ArrayList<>();

        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.HOUR, 1);

        Event event1 = new Event();

        event1.setTitle("C++");
        event1.setDescription("A wonderful journey!");
        event1.setLocation("AM-18");
        event1.setEventColor(Color.DKGRAY);
        event1.setStartDate(Utils.calendarToDate(startTime1));
        event1.setFinishDate(Utils.calendarToDate(endTime1));
        event1.setAllDay(false);
        event1.setReminder(true);
        event1.setEditable(false);

        startTime1 = Calendar.getInstance();
        startTime1.add(Calendar.HOUR, 2);
        endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.HOUR, 4);

        Event event2 = new Event();

        event2.setTitle("Android");
        event2.setDescription("A wonderful journey!");
        event2.setLocation("AM-18");
        event2.setEventColor(Color.DKGRAY);
        event2.setStartDate(Utils.calendarToDate(startTime1));
        event2.setFinishDate(Utils.calendarToDate(endTime1));
        event2.setAllDay(false);
        event2.setReminder(false);
        event2.setEditable(false);

        startTime1 = Calendar.getInstance();
        startTime1.add(Calendar.DATE, 1);
        endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.DATE, 1);
        endTime1.add(Calendar.HOUR, 1);

        Event event3 = new Event();

        event3.setTitle("Réunion");
        event3.setDescription("Project android");
        event3.setLocation("AM-16");
        event3.setEventColor(ResourcesCompat.getColor(ctx.getResources(), R.color.dark_green, null));
        event3.setStartDate(Utils.calendarToDate(startTime1));
        event3.setFinishDate(Utils.calendarToDate(endTime1));
        event3.setEditable(true);
        event3.setAllDay(false);
        event3.setReminder(true);

        AddEvent(event1);
        AddEvent(event2);
        AddEvent(event3);


    }
    public static EventRepository getInstance(Context ctx) {
        if(instance == null) {
            instance = new EventRepository(ctx);
        }
        return instance;
    }

    public Event GetEventById(int id)
    {
        for(Event event : eventList)
        {
            if(event.getId() == id)
            {
                return event;
            }
        }
        return null;
    }

    public List<Event> GetAllEventsByUserId(int id)
    {
        return eventList;
    }

    public List<Event> GetAll(int id)
    {
        return eventList;
    }

    public boolean AddEvent(Event event)
    {
        event.setId(mId++);
        eventList.add(event);

        if(event.isReminder())
        {
            Notifications n = new Notifications(context);
            n.createNotificationAtDate(event.getId(), event.getStartDate(), "Colval Agenda", "Votre événement " + event.getTitle() + " commence!");
        }

        return true;
    }

    public boolean DeleteEvent(int id)
    {
        for(int i=0;i<eventList.size();i++)
        {
            if(eventList.get(i).getId()==id)
            {
                eventList.remove(i);

                Notifications n = new Notifications(context);
                n.deleteNotification(id);

                return true;
            }
        }
        return false;
    }

    public boolean EditEvent(int id, Event newEvent)
    {
        for(int i=0;i<eventList.size();i++)
        {
            if(eventList.get(i).getId()==id)
            {
                Event event = eventList.get(i);
                event.setTitle(newEvent.getTitle());
                event.setDescription(newEvent.getDescription());
                event.setLocation(newEvent.getLocation());
                event.setStartDate(newEvent.getStartDate());
                event.setFinishDate(newEvent.getFinishDate());
                boolean notificationBefore = event.isReminder();
                event.setReminder(newEvent.isReminder());

                if(event.isReminder())
                {
                    Notifications n = new Notifications(context);
                    n.createNotificationAtDate(event.getId(), event.getStartDate(), "Colval Agenda", "Votre événement " + event.getTitle() + " commence!");
                }
                else if(notificationBefore)
                {
                    Notifications n = new Notifications(context);
                    n.deleteNotification(event.getId());
                }

                return true;
            }
        }
        return false;
    }
}