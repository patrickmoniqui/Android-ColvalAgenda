package com.colval_agenda.BLL;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by macbookpro on 17-05-03.
 */

public class Event {
    private int Id;
    private String Title;
    private String Description;
    private String Location;
    private int EventColor;
    private Date startTime;
    private Date endTime;
    private boolean AllDay;
    private boolean notification;
    private boolean editable;

    public BaseCalendarEvent ToBaseCalendarEvent()
    {
        BaseCalendarEvent event = new BaseCalendarEvent();
        event.setId(Id);
        event.setTitle(Title);
        event.setLocation(Location);
        event.setDescription(Description);
        event.setColor(EventColor);
        event.setStartTime(Utils.dateToCalendar(startTime));
        event.setEndTime(Utils.dateToCalendar(endTime));
        event.setAllDay(AllDay);
        event.setNotification(notification);

        return event;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getEventColor() {
        return EventColor;
    }

    public void setEventColor(int eventColor) {
        EventColor = eventColor;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isAllDay() {
        return AllDay;
    }

    public void setAllDay(boolean allDay) {
        AllDay = allDay;
    }


    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean getNotification() {
        return notification;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }


}
