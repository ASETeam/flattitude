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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.aseupc.flattitude.InternalDatabase.AsyncTasks.AsyncAddPlanningTaskTask;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.database.PlanningTask_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        PlanningTask_Web_Services.AddPlanningTaskWSListener,
        AsyncAddPlanningTaskTask.OnTaskAddedListener
{

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private EditText date;
    private EditText time;
    private EditText description;
    private Spinner type;
    private Calendar calendar;
    private ArrayAdapter<String> adapter;
    private View mProgressView;
    private Dialog mOverlayDialog; //display an invisible overlay dialog to prevent user interaction and pressing back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);

        calendar = (Calendar) getIntent().getSerializableExtra("date");
        if(calendar==null)
            calendar = Calendar.getInstance();

        final NewTaskActivity finalThis = this;

        date = (EditText) findViewById(R.id.date);
        date.setText(dateFormat.format(calendar.getTime()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setListener(finalThis);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        time = (EditText) findViewById(R.id.time);
        time.setText(timeFormat.format(calendar.getTime()));
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setListener(finalThis);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        description = (EditText) findViewById(R.id.description);
        type = (Spinner) findViewById(R.id.type);
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, PlanningTask.getTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);


        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });


        mOverlayDialog = new Dialog(this, android.R.style.Theme_Panel);
        mOverlayDialog.setCancelable(false);
        mProgressView = findViewById(R.id.progressBar);
        showProgress(false);
    }

    private void addTask(){
        //showProgress(true);
        PlanningTask task = new PlanningTask();
        task.setPlannedTime(calendar);
        task.setDescription(description.getText().toString());
        task.setType(type.getSelectedItem().toString());
        task.setAuthor(IDs.getInstance(this).getUserId(this));
        IDs ids = IDs.getInstance(this);
        PlanningTask_Web_Services ws = new PlanningTask_Web_Services();
        ws.ws_addTask(task, ids.getUserId(this), ids.getUserToken(this), ids.getFlatId(this), this);
    }

    public Calendar getCalendar(){
        return calendar;
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

    @Override
    public void onAddWSFinished(ResultContainer<PlanningTask> result) {
        if(result.getSucces()){
            //Internal database
            AsyncAddPlanningTaskTask async = new AsyncAddPlanningTaskTask(result.getTemplate(),this);
            async.execute();
        }
        else {
            //showProgress(false);
            showTaskNotAdded();
        }
    }

    @Override
    public void OnAddedToDatabase(PlanningTask task) {
        //showProgress(false);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("task",task);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void showTaskNotAdded()
    {
        new AlertDialog.Builder(this)
                .setTitle("Addition failed")
                .setMessage("The task couldn't be added. Check your internet connection and try again")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static class TimePickerFragment extends DialogFragment {
        NewTaskActivity listener;

        public void setListener(NewTaskActivity listener){
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = listener.getCalendar();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), listener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if(show) mOverlayDialog.show();
        else mOverlayDialog.hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public static class DatePickerFragment extends DialogFragment {

        NewTaskActivity listener;

        public void setListener(NewTaskActivity listener){
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = listener.getCalendar();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }

    }

}
