package com.aseupc.flattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
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

import com.aseupc.flattitude.InternalDatabase.AsyncTasks.AsyncAddPlanningTaskTask;
import com.aseupc.flattitude.InternalDatabase.AsyncTasks.AsyncEditPlanningTaskTask;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.database.PlanningTask_Web_Services;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditTaskActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        PlanningTask_Web_Services.EditPlanningTaskWSListener,
        AsyncEditPlanningTaskTask.OnTaskEditedListener
{

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private EditText date;
    private EditText time;
    private EditText timeReminder;
    private EditText dateReminder;
    private EditText description;
    private Spinner type;
    private Calendar calendar;
    private Calendar reminder;
    private PlanningTask task;
    private Calendar originalDate;
    private ArrayAdapter<String> adapter;
    private View mProgressView;
    private Dialog mOverlayDialog; //display an invisible overlay dialog to prevent user interaction and pressing back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);
        SpannableString s = new SpannableString("Flattitude");
        s.setSpan(new ForegroundColorSpan(Color.rgb(33, 33, 33)), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new TypefaceSpan("Montserrat-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);

        task = (PlanningTask) getIntent().getSerializableExtra("task");
        calendar = task.getPlannedTime();
        originalDate = Calendar.getInstance();
        originalDate.setTime(task.getPlannedTime().getTime());

        final EditTaskActivity finalThis = this;

        date = (EditText) findViewById(R.id.date);
        date.setText(dateFormat.format(calendar.getTime()));
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
        time.setText(timeFormat.format(calendar.getTime()));
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setListener(finalThis);
                newFragment.setCalendar(calendar);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        description = (EditText) findViewById(R.id.description);
        description.setText(task.getDescription());

        type = (Spinner) findViewById(R.id.type);
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, PlanningTask.getTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setSelection(adapter.getPosition(task.getType()));

        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask();
            }
        });


        mOverlayDialog = new Dialog(this, android.R.style.Theme_Panel);
        mOverlayDialog.setCancelable(false);
        mProgressView = findViewById(R.id.progressBar);
        showProgress(false);
    }

    private void editTask(){
        task.setPlannedTime(calendar);
        task.setDescription(description.getText().toString());
        task.setType(type.getSelectedItem().toString());
        IDs ids = IDs.getInstance(this);
        PlanningTask_Web_Services ws = new PlanningTask_Web_Services();
        ws.ws_editTask(task, ids.getUserId(this), ids.getUserToken(this), ids.getFlatId(this), this);
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
    public void onEditWSFinished(ResultContainer<PlanningTask> result) {
        if(result.getSucces()){
            //Internal database
            AsyncEditPlanningTaskTask async = new AsyncEditPlanningTaskTask(result.getTemplate(),this);
            async.execute();
        }
        else {
            showTaskNotEdited();
        }
    }

    @Override
    public void OnEditedOnDatabase(PlanningTask task) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("originalDate",originalDate);
        returnIntent.putExtra("task",task);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void showTaskNotEdited()
    {
        new AlertDialog.Builder(this)
                .setTitle("Edition failed")
                .setMessage("The task couldn't be edited. Check your internet connection and try again")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
