package com.github.tibolte.agendacalendarview.render;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.R;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class helping to inflate our default layout in the AgendaAdapter
 */
public class DefaultEventRenderer extends EventRenderer<BaseCalendarEvent> {

    // region class - EventRenderer

    @Override
    public void render(@NonNull View view, @NonNull BaseCalendarEvent event) {
        TextView txtTitle = (TextView) view.findViewById(R.id.view_agenda_event_title);
        TextView txtLocation = (TextView) view.findViewById(R.id.view_agenda_event_location);
        TextView txtTime = (TextView) view.findViewById(R.id.view_agenda_event_time);
        LinearLayout titleContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_title_container);

        LinearLayout descriptionContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_description_container);
        LinearLayout locationContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_location_container);
        LinearLayout timeContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_time_container);

        ImageView imgReminder = (ImageView) titleContainer.findViewById(R.id.view_agent_event_reminder);

        descriptionContainer.setVisibility(View.VISIBLE);
        txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));

        Calendar start=event.getStartTime();
        Calendar end=event.getEndTime();

        // Title
        txtTitle.setText(event.getTitle());

        //Reminder Icon
        if(event.getNotification())
        {
            imgReminder.setVisibility(View.VISIBLE);
        }
        else
        {
            imgReminder.setVisibility(View.GONE);
        }

        //Start/End Time
        if(start != null && end != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String title = dateFormat.format(event.getStartTime().getTime()) + " - " + dateFormat.format(event.getEndTime().getTime());
            txtTime.setText(title);
            timeContainer.setVisibility(View.VISIBLE);
        }
        else
        {
            timeContainer.setVisibility(View.GONE);
        }

        //Location
        txtLocation.setText(event.getLocation());
        if (event.getLocation().length() > 0) {
            locationContainer.setVisibility(View.VISIBLE);
            txtLocation.setText(event.getLocation());
        } else {
            locationContainer.setVisibility(View.GONE);
        }

        if (event.getTitle().equals(view.getResources().getString(R.string.agenda_event_no_events))) {
            txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));
        } else {
            txtTitle.setTextColor(view.getResources().getColor(R.color.theme_text_icons));
        }
        descriptionContainer.setBackgroundColor(event.getColor());
        txtLocation.setTextColor(view.getResources().getColor(R.color.theme_text_icons));
        txtTime.setTextColor(view.getResources().getColor(R.color.theme_text_icons));
    }

    @Override
    public int getEventLayout() {
        return R.layout.view_agenda_event;
    }

    // endregion
}
