package com.colval_agenda;

import com.colval_agenda.BLL.Event;
import com.colval_agenda.BLL.Response.EventResponse;
import com.colval_agenda.BLL.Response.LoginResponse;
import com.colval_agenda.Utils.Preferences;
import com.colval_agenda.Utils.Utils;
import com.colval_agenda.DAL.EventRepository;
import com.colval_agenda.DAL.HttpGetHandler;
import com.colval_agenda.DAL.LoginManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarManager;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.github.tibolte.agendacalendarview.models.IDayItem;
import com.github.tibolte.agendacalendarview.models.IWeekItem;
import com.github.tibolte.agendacalendarview.models.WeekItem;
import com.github.tibolte.colvalcalendar.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.colval_agenda.Event_Edit_Activity.RESULT_NO_REFRESH_NEEDED;
import static com.colval_agenda.Event_Edit_Activity.RESULT_REFRESH_NEEDED;

public class MainActivity extends AppCompatActivity implements CalendarPickerController {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    AgendaCalendarView mAgendaCalendarView;
    EventRepository eventRepository;
    List<Event> events;
    List<Event> classes;
    int numDA = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classes = new ArrayList<>();

        if(Utils.GlobalUserLogged(getBaseContext()))
        {
            numDA = Utils.GetGlobalUserId(getBaseContext());
        }
        else
        {
            RedirectToLogin();
        }

        mAgendaCalendarView = (AgendaCalendarView)findViewById(R.id.agenda_calendar_view);
        eventRepository = EventRepository.getInstance(getBaseContext());

        AsyncTask<Void, Void, EventResponse[]> eventAsync = new EventAsync(numDA);
        eventAsync.execute();
    }

    private void initAgenda()
    {
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.YEAR, -1);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        List<CalendarEvent> eventList = new ArrayList<>();

        for(Event event : events)
        {
            eventList.add(event.ToBaseCalendarEvent());
        }

        CalendarManager calendarManager = CalendarManager.getInstance(getApplicationContext());
        calendarManager.buildCal(minDate, maxDate, Locale.getDefault(), new DayItem(), new WeekItem());
        calendarManager.loadEvents(eventList, new BaseCalendarEvent());

        List<CalendarEvent> readyEvents = calendarManager.getEvents();
        List<IDayItem> readyDays = calendarManager.getDays();
        List<IWeekItem> readyWeeks = calendarManager.getWeeks();
        mAgendaCalendarView.init(Locale.getDefault(), readyWeeks,readyDays,readyEvents,this);
    }


    @Override
    public void onDaySelected(IDayItem dayItem) {
        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d(LOG_TAG, String.format("Selected event: %s", event));
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    @Override
    public void onEventLongPress(CalendarEvent calendar) {
        if(calendar.getEventColor() == ResourcesCompat.getColor(getResources(), R.color.courses_event_color, null))
        {
            Toast.makeText(this, getResources().getText(R.string.errorCantEditEvent), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(this, Event_Edit_Activity.class);
            intent.putExtra("eventid", calendar.getId()+"");
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onFloatingButtonAddEventClick() {
        Intent intent = new Intent(this, Event_Edit_Activity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(resultCode)
        {
            // 0 means there's no change. no refresh required.
            case RESULT_NO_REFRESH_NEEDED :
            {

            }
            // 1 means there is change. refresh required.
            case RESULT_REFRESH_NEEDED:
            {
                initAgenda();
                break;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void RedirectToLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public class EventArrayReponse {

        private List<EventResponse> events;

        public EventArrayReponse() {}

        public List<EventResponse> getEvents() {
            return events;
        }

        public void setEvents(List<EventResponse> events) {
            this.events = events;
        }
    }

    class EventAsync extends AsyncTask<Void, Void, EventResponse[]>
    {
        private int userId;
        ProgressDialog progressDialog;

        public EventAsync(int _username)
        {
            userId = _username;
            progressDialog = new ProgressDialog(MainActivity.this, R.style.Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Feetching events...");

        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected EventResponse[] doInBackground(Void... params) {

            try {
                final String url = "http://colvalagenda.gear.host/api/event/geteventsuserbyid?id=" + userId;
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                EventResponse[] response  = restTemplate.getForObject(url, EventResponse[].class);
                return response;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(EventResponse[] response) {

            if(response.length > 0)
            {

                events = eventRepository.GetAllEventsByUserId(numDA);
                for(EventResponse e : response)
                {
                    Event newEvent = new Event();
                    newEvent.setId(e.getId());
                    newEvent.setTitle(e.getTitle());
                    newEvent.setDescription(e.getDescription());
                    newEvent.setLocation(e.getLocation());
                    newEvent.setEventColor(ResourcesCompat.getColor(getResources(), R.color.courses_event_color, null)); //green pour les événements non editable
                    newEvent.setStartDate(e.getStartDate());
                    newEvent.setFinishDate(e.getFinishDate());
                    newEvent.setEditable(e.isEditable());
                    newEvent.setReminder(e.isReminder());
                    newEvent.setAllDay(e.isAllDay());
                    newEvent.setUserId(e.getUserId());
                    events.add(newEvent);
                }

                initAgenda();
            }
            else
            {

            }

            progressDialog.dismiss();
        }
    }
}

