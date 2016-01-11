package com.aseupc.flattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.aseupc.flattitude.InternalDatabase.AsyncTasks.AsyncEditPlanningTaskTask;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.database.PlanningTask_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SetAlarmActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener
{

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private EditText date;
    private EditText time;
    private Calendar calendar;
    private Calendar original;
    private PlanningTask task;
    private CheckBox reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        task = (PlanningTask) getIntent().getSerializableExtra("task");
        calendar = Calendar.getInstance();
        original = task.getAlarmTime();
        if(original != null)
            calendar.setTime(original.getTime());

        final SetAlarmActivity finalThis = this;

        date = (EditText) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setListener(finalThis);
                newFragment.setCalendar(calendar);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        time = (EditText) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setListener(finalThis);
                newFragment.setCalendar(calendar);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        date.setText(dateFormat.format(calendar.getTime()));
        time.setText(timeFormat.format(calendar.getTime()));

        reminder = (CheckBox) findViewById(R.id.alarmCheckBox);
        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkChanged(isChecked);
            }
        });

        if(original==null) {
            reminder.setChecked(false);
            checkChanged(false);
        }
        else{
            reminder.setChecked(true);
            checkChanged(true);
            date.setText(dateFormat.format(original.getTime()));
            time.setText(timeFormat.format(original.getTime()));
        }

        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

    }

    void checkChanged(boolean b){
        TextView timeTV = (TextView) findViewById(R.id.time_label);
        TextView dateTV = (TextView) findViewById(R.id.date_label);
        timeTV.setEnabled(b);
        dateTV.setEnabled(b);
        time.setEnabled(b);
        date.setEnabled(b);
    }

    private void setAlarm(){
        if(!reminder.isChecked())
            task.setAlarmTime(null);
        else task.setAlarmTime(calendar);
        PlanningDAO dao = new PlanningDAO(this);
        dao.update(task);

        if((original == null && task.getAlarmTime() != null) || !original.equals(task.getAlarmTime())) {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(this, AlarmReceiver.class);

            if(original!=null){
                PendingIntent pi = PendingIntent.getBroadcast(this, Integer.parseInt(task.getID()), i, PendingIntent.FLAG_CANCEL_CURRENT);
                //pi.cancel();
                am.cancel(pi);
            }
            if (reminder.isChecked()) {
                i.putExtra("task", task);
                PendingIntent pi = PendingIntent.getBroadcast(this, Integer.parseInt(task.getID()), i,0);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            }
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        time.setText(timeFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR, year);
        date.setText(dateFormat.format(calendar.getTime()));
    }

    public static class DatePickerFragment extends DialogFragment {

        DatePickerDialog.OnDateSetListener listener;
        Calendar c;

        public void setListener(DatePickerDialog.OnDateSetListener listener){
            this.listener = listener;
        }
        public void setCalendar(Calendar c){
            this.c = c;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }

    }

    public static class TimePickerFragment extends DialogFragment {
        TimePickerDialog.OnTimeSetListener listener;
        Calendar c;

        public void setListener(TimePickerDialog.OnTimeSetListener listener){
            this.listener = listener;
        }
        public void setCalendar(Calendar c){
            this.c = c;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), listener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

    }

}
