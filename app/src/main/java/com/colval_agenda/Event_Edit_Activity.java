package com.colval_agenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.colval_agenda.BLL.Event;
import com.colval_agenda.Utils.Utils;
import com.colval_agenda.DAL.EventRepository;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.colvalcalendar.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Event_Edit_Activity extends AppCompatActivity {
    EditText txtTitle, txtDesc, txtLocation, txtStartDate, txtStartTime, txtFinishTime;
    Button btnSave, btnClose, btnDelete;
    CheckBox ckRappeler;
    AgendaCalendarView mAgendaCalendarView;
    int userId=-1;
    int eventId=-1;
    Event event;
    EventRepository repository;

    public static final int RESULT_NO_REFRESH_NEEDED = 0;
    public static final int RESULT_REFRESH_NEEDED = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__edit_);

        //Authentification
        if (!Utils.GlobalUserLogged(getBaseContext())) {
            RedirectToLogin();
        }
        else
        {
            userId = Utils.GetGlobalUserId(getBaseContext());
        }

        repository = EventRepository.getInstance(getBaseContext());

        btnSave = (Button)findViewById(R.id.Event_Edit_btnAdd);
        btnClose = (Button)findViewById(R.id.Event_Edit_btnClose);
        btnDelete = (Button)findViewById(R.id.Event_Delete_btnDelete);

        txtTitle = (EditText)findViewById(R.id.Event_Edit_txtTitle);
        txtDesc = (EditText)findViewById(R.id.Event_Edit_txtDesc);
        txtLocation = (EditText)findViewById(R.id.Event_Edit_txtLocation);

        txtStartDate = (EditText)findViewById(R.id.EventRdv_txtStartDate);
        txtStartTime = (EditText)findViewById(R.id.Event_txtStartTime);
        txtFinishTime = (EditText)findViewById(R.id.EventRdv_txtFinishTime);

        ckRappeler = (CheckBox)findViewById(R.id.ckBoxRappeler);

        mAgendaCalendarView = (AgendaCalendarView)findViewById(R.id.agenda_calendar_view);

        SetupEventHandlers();

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            String id = extras.getString("eventid");
            eventId = Integer.parseInt(id);

            if(eventId > 0)
            {
                event = repository.GetEventById(eventId);

                // si l'évènement n'est pas modification
                if(!event.isEditable())
                {
                    Toast.makeText(this, getResources().getText(R.string.errorCantEditEvent), Toast.LENGTH_SHORT).show();
                    finish();
                }

                SetupInterfaceForEdit();
                btnSave.setText(getResources().getString(R.string.save));
                btnDelete.setVisibility(View.VISIBLE);
            }
            else
            {
                btnSave.setText(getResources().getString(R.string.add));
                btnDelete.setVisibility(View.GONE);
            }
        }
        else
        {
            btnSave.setText(getResources().getString(R.string.add));
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void SetupInterfaceForEdit()
    {
        //initialise
        Date date = event.getStartDate();
        Calendar mcurrentDate=Calendar.getInstance();
        mcurrentDate.setTime(date);
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
        int fhour = mcurrentDate.get(Calendar.HOUR_OF_DAY);
        int fminute = mcurrentDate.get(Calendar.MINUTE);

        date = event.getFinishDate();
        mcurrentDate=Calendar.getInstance();
        mcurrentDate.setTime(date);
        int thour = mcurrentDate.get(Calendar.HOUR_OF_DAY);
        int tminute = mcurrentDate.get(Calendar.MINUTE);

        txtTitle.setText(event.getTitle());
        txtDesc.setText(event.getDescription());
        txtLocation.setText(event.getLocation());
        txtStartDate.setText(Utils.padLeft(mDay, "0", 2) + "/" + Utils.padLeft(mMonth+1, "0", 2) + "/" + Utils.padLeft(mYear, "0", 2));
        txtStartTime.setText(Utils.padLeft(fhour, "0", 2) + ":" + Utils.padLeft(fminute, "0", 2));
        txtFinishTime.setText(Utils.padLeft(thour, "0", 2) + ":" + Utils.padLeft(tminute, "0", 2));
        ckRappeler.setChecked(event.isReminder());
    }

    private void SetupEventHandlers()
    {
        View.OnClickListener onClickListener_DaterDialog = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay;
                final EditText editText = (EditText)v;
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(Event_Edit_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
                        editText.setText(Utils.padLeft(selectedday, "0", 2) + "/" + Utils.padLeft(selectedmonth+1, "0", 2) + "/" + Utils.padLeft(selectedyear, "0", 2));
                    }
                },mYear, mMonth, mDay);
                mDatePicker.show();
            }
        };

        View.OnClickListener onClickListener_TimeDialog = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = (EditText)v;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Event_Edit_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editText.setText(Utils.padLeft(selectedHour, "0", 2) + ":" + Utils.padLeft(selectedMinute, "0", 2));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        };

        txtStartDate.setOnClickListener(onClickListener_DaterDialog);

        txtStartTime.setOnClickListener(onClickListener_TimeDialog);

        txtFinishTime.setOnClickListener(onClickListener_TimeDialog);
    }

    public void AddEvent_btnAdd_OnClick(View v)
    {
        if (ValidateForm())
        {
            String title = txtTitle.getText().toString();
            String desc = txtDesc.getText().toString();
            String location = txtLocation.getText().toString();

            Date startDate = Utils.parseDate("dd/MM/yyyy", txtStartDate.getText().toString());
            Time startTime = Utils.parseTime("hh:mm", txtStartTime.getText().toString());
            Time finishTime = Utils.parseTime("hh:mm", txtFinishTime.getText().toString());

            boolean notification = ckRappeler.isChecked();

            Event event = new Event();
            event.setTitle(title);
            event.setDescription(desc);
            event.setLocation(location);
            event.setEventColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_blue)); //DARK BLUE = CUSTOM EVENT ADDED BY USER

            event.setStartDate(Utils.addTimeToDate(startDate, startTime));
            event.setFinishDate(Utils.addTimeToDate(startDate, finishTime));
            event.setReminder(ckRappeler.isChecked());

            // if modification event
            if(eventId > 0)
            {
                repository.EditEvent(eventId, event);
            }
            else
            {
                repository.AddEvent(event);
            }

            setResult(RESULT_REFRESH_NEEDED);
            finish();
        }
    }

    public void Event_btnCancel_OnClick(View v)
    {
        setResult(RESULT_NO_REFRESH_NEEDED);
        finish();
    }

    public void Event_btnDelete_OnClick(View v)
    {
        boolean b = repository.DeleteEvent(eventId);
        if(!b)
        {
            Toast.makeText(this, getResources().getText(R.string.errorDeleteEvent), Toast.LENGTH_LONG).show();
        }
        else
        {
            setResult(RESULT_REFRESH_NEEDED);
            finish();
        }
    }

    public boolean ValidateForm()
    {
        String errMsg = "";

        if(txtTitle.getText().toString() == "")
        {
            errMsg += "Entrer un titre. \n";
        }

        if (txtStartDate.getText().toString() == "")
        {
            errMsg += "Entrer une date de départ valide. \n";
        }

        if (txtStartTime.getText().toString() == "")
        {
            errMsg += "Enter l'heure de début. \n";
        }

        if (txtFinishTime.getText().toString() == "")
        {
            errMsg += "Entrer l'heure de fin. \n";
        }

        if (errMsg == "")
        {
            return true;
        }
        else
        {
            Toast.makeText(this, errMsg.substring(0, errMsg.length() - 1), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void RedirectToLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}