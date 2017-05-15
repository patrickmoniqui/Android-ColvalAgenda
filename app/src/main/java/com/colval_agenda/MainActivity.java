package com.colval_agenda;

import com.colval_agenda.BLL.Event;
import com.colval_agenda.Utils.Preferences;
import com.colval_agenda.Utils.Utils;
import com.colval_agenda.DAL.EventRepository;
import com.colval_agenda.DAL.HttpGetHandler;
import com.colval_agenda.DAL.LoginManager;
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

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
    List<Event> classes;
    int numDA;
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

        initAgenda();
    }

    private void initAgenda()
    {
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        List<CalendarEvent> eventList = new ArrayList<>();
        List<Event> events = eventRepository.GetAllEventsByUserId(numDA);

        new GetClassesAsyncTask().execute();

        for (int i = 0; i < classes.size(); i++){
            if (classes.get(i).getUserId() == numDA){
                events.add(classes.get(i));
            }
        }

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
        Intent intent = new Intent(this, Event_Edit_Activity.class);
        intent.putExtra("eventid", calendar.getId()+"");
        startActivityForResult(intent, 0);
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

    class GetClassesAsyncTask extends AsyncTask <Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpGetHandler httpGetHandler = new HttpGetHandler();
            classes = httpGetHandler.callGetWebService(Preferences.URL_GET_CLASSES);
            return null;
        }
    }
}

