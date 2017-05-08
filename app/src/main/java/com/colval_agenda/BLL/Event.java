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
    private Date startDate;
    private Date finishDate;
    private boolean AllDay;
    private boolean reminder;
    private boolean editable;
    private int userId;

    public BaseCalendarEvent ToBaseCalendarEvent()
    {
        BaseCalendarEvent event = new BaseCalendarEvent();
        event.setId(Id);
        event.setTitle(Title);
        event.setLocation(Location);
        event.setDescription(Description);
        event.setColor(EventColor);
        event.setStartTime(Utils.dateToCalendar(startDate));
        event.setEndTime(Utils.dateToCalendar(finishDate));
        event.setAllDay(AllDay);
        event.setNotification(reminder);

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isAllDay() {
        return AllDay;
    }

    public void setAllDay(boolean allDay) {
        AllDay = allDay;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
