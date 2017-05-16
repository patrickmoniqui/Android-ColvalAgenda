package com.colval_agenda.BLL.Response;

import com.colval_agenda.DAL.EventRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by macbookpro on 17-05-16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponse {
    @JsonProperty("Id")
    private int Id;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("Location")
    private String Location;
    @JsonProperty("EventColor")
    private int EventColor;
    @JsonProperty("startDate")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")
    @JsonProperty("finishDate")
    private Date finishDate;
    @JsonProperty("AllDay")
    private boolean AllDay;
    @JsonProperty("reminder")
    private boolean reminder;
    @JsonProperty("editable")
    private boolean editable;
    @JsonProperty("userId")
    private int userId;

    public EventResponse() {}

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
